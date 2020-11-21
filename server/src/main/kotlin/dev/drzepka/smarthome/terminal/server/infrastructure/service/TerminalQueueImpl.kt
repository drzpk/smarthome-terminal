package dev.drzepka.smarthome.terminal.server.infrastructure.service

import dev.drzepka.smarthome.terminal.common.transport.Side
import dev.drzepka.smarthome.terminal.common.transport.message.Message
import dev.drzepka.smarthome.terminal.common.transport.message.MessageList
import dev.drzepka.smarthome.terminal.common.transport.message.MessageResponse
import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import dev.drzepka.smarthome.terminal.server.domain.service.TerminalQueue
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.future.asCompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class TerminalQueueImpl(handler: TerminalQueue.Handler) : TerminalQueue {

    override var handler: TerminalQueue.Handler? = null

    private val log by Logger()
    private val messageResponseTimeout = 3000L

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val clientDatas = ConcurrentHashMap<Int, ClientData>()

    init {
        this.handler = handler
    }

    override suspend fun <Response : MessageResponse> putMessage(client: Client, message: Message<Response>): Response {
        if (log.isTraceEnabled)
            log.trace("Putting message from client ${client.id}: $message")

        return when (message.receiverSide) {
            Side.CLIENT -> processMessageFromServer(client, message)
            Side.SERVER -> processMessageFromClient(client, message)
        }
    }

    /**
     * Get messages from server waiting to be processed. By default each message is returned only once.
     *
     * @param client client for which message will be returned
     * @param all returns all messages, including those already returned by this method.
     */
    fun getQueuedMessages(client: Client, all: Boolean = false): MessageList {
        return coroutineScope.async {
            val clientData = clientDatas[client.id] ?: return@async MessageList()

            val unsent = clientData.queue.values.filter { all || !clientData.sentMessages.contains(it.id) }
            clientData.sentMessages.addAll(unsent.map { it.id })
            MessageList(unsent)
        }.asCompletableFuture().get(3, TimeUnit.SECONDS)
    }

    fun provideResponses(client: Client, responses: Collection<MessageResponse>) {
        coroutineScope.launch {
            responses.forEach { respondToMessage(client, it) }
        }
    }

    private suspend fun <Response : MessageResponse> processMessageFromServer(
        client: Client,
        message: Message<Response>
    ): Response {

        val clientData = clientDatas.computeIfAbsent(client.id) { ClientData() }
        addMessageToQueue(clientData, message)

        try {
            return withTimeout(messageResponseTimeout) {
                val channel = Channel<MessageResponse>()
                clientData.waitingChannels[message.id] = channel

                @Suppress("UNCHECKED_CAST")
                channel.receive() as Response
            }
        } finally {
            clientData.waitingChannels.remove(message.id)

            if (clientData.queue.isEmpty())
                clientDatas.remove(client.id)
        }
    }

    private fun <Response : MessageResponse> processMessageFromClient(client: Client, message: Message<Response>): Response {
        return handler!!.processMessage(client, message)
    }

    private fun respondToMessage(client: Client, messageResponse: MessageResponse) {
        val clientData = clientDatas[client.id]
        if (clientData == null) {
            log.error("Client data for $client wasn't found")
            return
        }

        val id = messageResponse.requestMessageId
        val channel = clientData.waitingChannels.remove(id)

        if (channel != null) {
            channel.sendBlocking(messageResponse)
        } else {
            log.error("Unable to respond to message $id with ${messageResponse::class.java.simpleName} because the original message wasn't found in the queue")
        }

        if (clientData.queue.remove(id) != null && channel == null)
            log.warn("Inconsistency detected: queue contained message $id but there was no channel associated with it")
        if (clientData.sentMessages.remove(id) && channel == null)
            log.warn("Inconsistency detected: sentMessages contained message $id but there was no channel associated with it")
    }

    private fun addMessageToQueue(client: ClientData, message: Message<out MessageResponse>) {
        if (client.queue.containsKey(message.id)) {
            // Integrity check
            throw RuntimeException("queue already contains message with id ${message.id}")
        }

        client.queue[message.id] = message
    }

    private class ClientData {
        val queue = ConcurrentHashMap<Long, Message<out MessageResponse>>()
        val sentMessages = HashSet<Long>()
        val waitingChannels = ConcurrentHashMap<Long, Channel<MessageResponse>>()
    }
}