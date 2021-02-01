package dev.drzepka.smarthome.terminal.client.manager

import dev.drzepka.smarthome.terminal.client.transport.queue.ClientHttpTerminalQueue
import dev.drzepka.smarthome.terminal.client.transport.queue.TerminalQueue
import dev.drzepka.smarthome.terminal.common.transport.message.*
import dev.drzepka.smarthome.terminal.common.util.IdSpace
import dev.drzepka.smarthome.terminal.common.util.Logger
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class ClientManager : TerminalQueue.Handler, IdSpace {
    abstract val clientName: String
    override var idSpaceState = 0

    protected var initialized = false
        private set

    private val log by Logger()
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

        screenManager.id = nextId()
        screenManagers.add(screenManager)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <Response : MessageResponse> processMessage(message: Message<Response>): Response {
        if (log.isTraceEnabled)
            log.trace("Processing message: ${message.javaClass.simpleName}")

        return when (message) {
            is PingClientMessage -> processPingMessage(message) as Response
            is GetCategoriesMessage -> processGetCategoriesMessage(message) as Response
            is GetScreenMessage -> processGetScreenMessage(message) as Response
            is ScreenUpdateMessage -> processScreenUpdateMessage(message) as Response
            else -> throw IllegalStateException("Unknown message: $message")
        }
    }

    private fun processPingMessage(message: PingClientMessage): PingClientMessageResponse {
        return PingClientMessageResponse(message)
    }

    private fun processGetCategoriesMessage(message: GetCategoriesMessage): GetCategoriesMessageResponse {
        val response = GetCategoriesMessageResponse(message)
        response.categories.addAll(screenManagers.map { it.category })
        return response
    }

    private fun processGetScreenMessage(message: GetScreenMessage): GetScreenMessageResponse {
        // ScreenID = CategoryID (at least for now)
        val screenId = message.categoryId

        val manager = screenManagers.firstOrNull { it.id == screenId }
        if (manager == null) {
            log.warn("The terminal server has requested a screen with id={$screenId, but it wasn't found")
            return GetScreenMessageResponse(message) // Resulting screen is null by default which means "not found"
        }

        log.info("The terminal server requested a screen with id=$screenId")
        return try {
            val response = GetScreenMessageResponse(message)
            response.screen = manager.getScreen().apply {
                id = manager.category.id
            }
            response
        } catch (e: Exception) {
            log.error("Error while getting screen with id=$screenId", e)
            GetScreenMessageResponse(message)
        }
    }

    private fun processScreenUpdateMessage(message: ScreenUpdateMessage): ScreenUpdateMessageResponse {
        log.debug(
            "Processing screen update message for screen id {} with {} properties",
            message.screenId,
            message.propertyValues.size
        )

        val screenManager = screenManagers.firstOrNull { it.id == message.screenId }
        if (screenManager == null) {
            log.error("Screen with id {} wasn't found", message.screenId)
            return ScreenUpdateMessageResponse(message).apply {
                status = ScreenUpdateMessageResponse.Status.ERROR
                this.message = "Screen wasn't found"
            }
        }

        return screenManager.processUpdate(message)
    }

    private fun ensureNotInitialized() {
        if (initialized)
            throw IllegalStateException("Can't modify client manager after initialization")
    }
}
