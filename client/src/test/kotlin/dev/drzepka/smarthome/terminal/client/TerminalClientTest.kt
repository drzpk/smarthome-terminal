package dev.drzepka.smarthome.terminal.client

import dev.drzepka.smarthome.terminal.client.manager.ClientManager
import dev.drzepka.smarthome.terminal.common.configurer.JacksonConfigurer
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class TerminalClientTest {

    private var registered = false
    private var unregistered = false

    @Test
    fun `should register and unregister client`() {
        val manager = TestManager()
        val client = TestTerminalClient("http://localhost:8888", manager)

        client.register()
        then(registered).isTrue()
        then(manager.isInitialized()).isTrue()

        client.unregister()
        then(unregistered).isTrue()
    }

    private inner class TestTerminalClient(terminalApiUrl: String, manager: ClientManager) :
        TerminalClient(terminalApiUrl, manager) {
        override fun createHttpClient(): HttpClient = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    when (request.url.fullPath) {
                        "/clients/register" -> {
                            registered = true
                            respond("{}", headers = headersOf("Content-Type", "application/json"))
                        }
                        "/clients/unregister" -> {
                            unregistered = true
                            respond("{}", headers = headersOf("Content-Type", "application/json"))
                        }
                        else -> error("unhandled path: ${request.method} ${request.url.fullPath}")
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
    }

    private class TestManager : ClientManager() {
        override val clientName: String = "clientName"
        private var initializedCalled = false

        override fun onInitialize() {
            initializedCalled = true
        }

        fun isInitialized(): Boolean = initialized && initializedCalled
    }
}