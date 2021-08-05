package dev.drzepka.smarthome.terminal.client.transport.queue

import dev.drzepka.smarthome.terminal.common.transport.Side
import dev.drzepka.smarthome.terminal.common.transport.message.Message
import dev.drzepka.smarthome.terminal.common.transport.message.MessageList
import dev.drzepka.smarthome.terminal.common.transport.message.MessageResponse
import dev.drzepka.smarthome.terminal.common.util.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketException
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Client sends messages normally, but periodically polls server for remote messages
 */
class ClientHttpTerminalQueue(
    private val httpClient: HttpClient,
    terminalApiUrl: String,
    private val coroutineScope: CoroutineScope,
    override var handler: TerminalQueue.Handler?,
    private val pollIntervalMs: Long = 1000L,
    /** Maximum time to wait for server to come online if it's unavailable. The counter resets after first normal response from the server. */
    private val pollErrorMaxDurationMs: Long = -1
) : TerminalQueue {

    private val log by Logger()
    private val serverQueueUrl = "$terminalApiUrl/terminal/queue"
    private val serverPollQueueUrl = "$terminalApiUrl/terminal/queue/poll"

    private var working = AtomicBoolean(false)
    private var queueStopListener: ((e: Exception?) -> Unit)? = null
    private var firstErrorTime: Instant? = null

    fun start(stopListener: ((e: Exception?) -> Unit)? = null) {
        if (!working.get()) {
            log.info("Starting the queue")
            working.set(true)
            queueStopListener = stopListener
            startPolling()
        }
    }

    fun stop() {
        doStop(null)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <Response : MessageResponse> putMessage(message: Message<Response>): Response {
        if (message.receiverSide == Side.CLIENT)
            throw UnsupportedOperationException("Cannot send messages to client")

        val builder = HttpRequestBuilder()
            .apply {
                url(serverQueueUrl)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                body = message
                timeout {
                    socketTimeoutMillis = 3000
                }
            }

        val response = httpClient.put<HttpResponse>(builder)
        return response.receive<MessageResponse>() as Response
    }

    private fun startPolling() {
        coroutineScope.launch {
            while (working.get()) {
                try {
                    log.trace("Polling terminal queue for messages")
                    val response = httpClient.get<MessageList>(serverPollQueueUrl)
                    log.trace("Received ${response.size} messages")
                    response.forEach { handleMessageFromServer(it) }

                    firstErrorTime = null
                    delay(pollIntervalMs)
                } catch (e: Exception) {
                    val continueWorking = handlePollingException(e)
                    if (!continueWorking)
                        break
                }
            }
        }
    }

    private suspend fun handleMessageFromServer(message: Message<out MessageResponse>) {
        handler?.processMessage(message)?.also { messageResponse ->
            httpClient.put(serverPollQueueUrl) {
                contentType(ContentType.Application.Json)
                body = messageResponse
            }
        }
    }

    private suspend fun handlePollingException(e: Exception): Boolean {
        if (e is SocketException) {
            val errorTimeWasNull = firstErrorTime == null
            if (errorTimeWasNull) {
                log.error("Error in poll routine", e)
                firstErrorTime = Instant.now()
            } else {
                log.error("Error in poll routine: {}", e.message)
            }

            val duration = Duration.between(firstErrorTime, Instant.now()).toMillis()
            val maxTimeExceeded = pollErrorMaxDurationMs in 1 until duration

            if (!errorTimeWasNull && !maxTimeExceeded) {
                val timeToWait = 3000L
                log.info("Waitining {}ms before retrying", timeToWait)
                delay(timeToWait)
            }

            if (maxTimeExceeded) {
                log.error("Maximum poll error duration ({}) has been exceeded ({}), stopping the queue", pollErrorMaxDurationMs, duration)
                doStop(e)
                return false
            }
        } else {
            doStop(e)
            return false
        }

        return true
    }

    private fun doStop(exception: Exception?) {
        if (working.get()) {
            log.info("Stopping the queue")
            working.set(false)
            queueStopListener?.invoke(exception)
        }
    }
}