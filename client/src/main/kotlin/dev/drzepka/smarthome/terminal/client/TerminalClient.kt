package dev.drzepka.smarthome.terminal.client

import dev.drzepka.smarthome.terminal.client.data.TerminalClientIdentity
import dev.drzepka.smarthome.terminal.client.manager.ClientManager
import dev.drzepka.smarthome.terminal.client.transport.queue.ClientHttpTerminalQueue
import dev.drzepka.smarthome.terminal.common.api.clients.RegisterClientRequest
import dev.drzepka.smarthome.terminal.common.api.clients.RegisterClientResponse
import dev.drzepka.smarthome.terminal.common.api.clients.UnregisterClientRequest
import dev.drzepka.smarthome.terminal.common.api.clients.UnregisterClientResponse
import dev.drzepka.smarthome.terminal.common.configurer.JacksonConfigurer
import dev.drzepka.smarthome.terminal.common.util.Logger
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlin.concurrent.thread

open class TerminalClient(private val identity: TerminalClientIdentity, private val clientManager: ClientManager) {

    private val log by Logger()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private lateinit var httpClient: HttpClient
    private lateinit var terminalQueue: ClientHttpTerminalQueue
    private var apiKey: String? = null

    /**
     * Registers this client in the terminal server.
     */
    fun register() {
        // Reset HTTP client so it doesn't contain authorization
        apiKey = null
        httpClient = createHttpClient()

        log.info("Registering terminal client {}", clientManager.clientName)
        runBlocking {
            doRegister()
        }

        createShutdownHook()
        httpClient = createHttpClient()

        terminalQueue = ClientHttpTerminalQueue(httpClient, identity.apiUrl.toString(), coroutineScope, clientManager)
        terminalQueue.start(this::onQueueStopped)

        clientManager.initialize()
    }

    /**
     * Unregisters this client from the terminal server. Called automatically on JVM exit
     */
    fun unregister() {
        log.info("Unregistering terminal client {}", clientManager.clientName)
        terminalQueue.stop()

        runBlocking {
            try {
                doUnregister()
            } catch (e: Exception) {
                log.error("Failed to unregister client.", e)
            }
        }
    }

    open fun createHttpClient(): HttpClient = HttpClient(Apache) {
        if (apiKey != null) {
            defaultRequest {
                headers {
                    set("Authorization", "Bearer $apiKey")
                }
            }
        }

        install(JsonFeature) {
            serializer = JacksonSerializer {
                JacksonConfigurer.configure(this)
            }
        }
        install(HttpTimeout)
    }

    private suspend fun doRegister() {
        val registrationAttempts = 3
        var response: RegisterClientResponse? = null
        for (i in 1..registrationAttempts) {
            try {
                response = tryToRegister()
                break
            } catch (e: Exception) {
                log.warn("Failed to register client (attempt $i/$registrationAttempts)")
                log.debug("Exception details", e)
                if (i == registrationAttempts) {
                    log.error("Client registration attempt count reached")
                    throw IllegalStateException("Client registration failed", e)
                } else {
                    delay(1000)
                }
            }
        }

        log.info("Client registered with id {}", response!!.clientId)
        log.debug("Full response: {}", response)
        apiKey = response.apiKey
    }

    private suspend fun tryToRegister(): RegisterClientResponse {
        return httpClient.post("${identity.apiUrl}/clients/register") {
            contentType(ContentType.Application.Json)
            body = RegisterClientRequest(identity.name, identity.password)
        }
    }

    private suspend fun doUnregister() {
        httpClient.post<UnregisterClientResponse>("${identity.apiUrl}/clients/unregister") {
            contentType(ContentType.Application.Json)
            body = UnregisterClientRequest()
        }
    }

    private fun createShutdownHook() {
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            clientManager.destroy()
            unregister()
        })
    }

    private fun onQueueStopped(stopException: Exception?) {
        if (stopException == null) {
            // Queue was stopped normally
            return
        }

        log.error("Queue was stopped abnormally, terminal client will be recreated", stopException)
        register()
    }
}