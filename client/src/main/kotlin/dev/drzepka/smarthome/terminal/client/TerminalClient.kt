package dev.drzepka.smarthome.terminal.client

import dev.drzepka.smarthome.terminal.client.manager.ClientManager
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
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

open class TerminalClient constructor(private val terminalApiUrl: String, private val clientManager: ClientManager) {

    private val log by Logger()
    private val httpClient by lazy { createHttpClient() }

    /**
     * Registers this client in the terminal server.
     */
    fun register() {
        log.info("Registering terminal client {}", clientManager.clientName)
        runBlocking {
            httpClient.post<RegisterClientResponse>("$terminalApiUrl/clients/register") {
                contentType(ContentType.Application.Json)
                body = RegisterClientRequest(clientManager.clientName, "TODO: secret")
            }
        }

        createShutdownHook()
        clientManager.initialize(httpClient, terminalApiUrl)
    }

    /**
     * Unregisters this client from the terminal server. Called automatically on JVM exit
     */
    fun unregister() {
        log.info("Unregistering terminal client {}", clientManager.clientName)
        runBlocking {
            httpClient.post<UnregisterClientResponse>("$terminalApiUrl/clients/unregister") {
                contentType(ContentType.Application.Json)
                body = UnregisterClientRequest()
            }
        }
    }

    open fun createHttpClient(): HttpClient = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                JacksonConfigurer.configure(this)
            }
        }
        install(HttpTimeout)
    }

    private fun createShutdownHook() {
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            clientManager.destroy()
            unregister()
        })
    }
}