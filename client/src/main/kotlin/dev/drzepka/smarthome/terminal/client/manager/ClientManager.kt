package dev.drzepka.smarthome.terminal.client.manager

import dev.drzepka.smarthome.terminal.client.transport.queue.ClientHttpTerminalQueue
import dev.drzepka.smarthome.terminal.client.transport.queue.TerminalQueue
import dev.drzepka.smarthome.terminal.common.transport.message.*
import dev.drzepka.smarthome.terminal.common.util.Logger
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class ClientManager : TerminalQueue.Handler {
    private val log by Logger()

    protected var initialized = false
        private set
    abstract val clientName: String

    private val screenManagers = ArrayList<ScreenManager>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var terminalQueue: ClientHttpTerminalQueue

    fun initialize(httpClient: HttpClient, terminalApiUrl: String) {
        log.info("Initializing client {}", clientName)

        terminalQueue = ClientHttpTerminalQueue(httpClient, terminalApiUrl, coroutineScope, this)
        terminalQueue.start()

        initialized = true
        onInitialize()
    }

    fun destroy() {
        terminalQueue.stop()
        coroutineScope.cancel()
    }

    /**
     * Initialized this client manager and registers all components.
     */
    abstract fun onInitialize()

    fun registerScreenManager(screenManager: ScreenManager) {
        ensureNotInitialized()
        val alreadyRegistered = screenManagers.any { screenManager.category == it.category }
        if (alreadyRegistered)
            throw IllegalArgumentException("Screen is already registered")

        screenManagers.add(screenManager)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <Response : MessageResponse> processMessage(message: Message<Response>): Response {
        //if (log.isTraceEnabled)
        log.info("Processing message: ${message.javaClass.simpleName}")

        return when (message) {
            is PingMessage -> processPingMessage(message) as Response
            is GetCategoriesMessage -> processGetCategoriesMessage(message) as Response
            else -> throw IllegalStateException("Unknown message: $message")
        }
    }

    private fun processPingMessage(message: PingMessage): PingMessageResponse {
        return PingMessageResponse(message)
    }

    private fun processGetCategoriesMessage(message: GetCategoriesMessage): GetCategoriesMessageResponse {
        val response = GetCategoriesMessageResponse(message)
        response.categories.addAll(screenManagers.map { it.category })
        return response
    }

    private fun ensureNotInitialized() {
        if (initialized)
            throw IllegalStateException("Can't modify client manager after initialization")
    }
}
