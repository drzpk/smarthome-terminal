package dev.drzepka.smarthome.terminal.client

import dev.drzepka.smarthome.terminal.client.data.TerminalClientIdentity
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
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

open class TerminalClient(private val identity: TerminalClientIdentity, private val clientManager: ClientManager) {

    private val log by Logger()
    private val httpClient by lazy { createHttpClient() }

    /**
     * Registers this client in the terminal server.
     */
    fun register() {
        log.info("Registering terminal client {}", clientManager.clientName)
        runBlocking {
            doRegister()
        }

        createShutdownHook()
        clientManager.initialize(httpClient, identity.apiUrl.toString())
    }

    /**
     * Unregisters this client from the terminal server. Called automatically on JVM exit
     */
    fun unregister() {
        log.info("Unregistering terminal client {}", clientManager.clientName)
        runBlocking {
            try {
                doUnregister()
            } catch (e: Exception) {
                log.error("Failed to unregister client.", e)
            }
        }
    }

    open fun createHttpClient(): HttpClient = HttpClient(Apache) {
        install(Auth) {
            basic {
                username = identity.name
                password = identity.password
                sendWithoutRequest = true
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
    }

    private suspend fun tryToRegister(): RegisterClientResponse {
        return httpClient.post("${identity.apiUrl}/clients/register") {
            contentType(ContentType.Application.Json)
            body = RegisterClientRequest()
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
}