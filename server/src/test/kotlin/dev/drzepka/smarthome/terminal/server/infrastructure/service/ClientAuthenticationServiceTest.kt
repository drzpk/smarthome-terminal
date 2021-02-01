package dev.drzepka.smarthome.terminal.server.infrastructure.service

import com.nhaarman.mockitokotlin2.mock
import com.typesafe.config.ConfigFactory
import dev.drzepka.smarthome.terminal.server.domain.service.EnvironmentService
import org.assertj.core.api.BDDAssertions.assertThatIllegalStateException
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class ClientAuthenticationServiceTest {

    private val clientName = "name"
    private val clientPassword = "password123456789"
    private val configString = """
            terminal {
              clients {
                registered = [
                  {
                    name = $clientName
                    password = $clientPassword
                  }
                ]
              }
            }
        """.trimIndent()

    @Test
    fun `should load clients configuration`() {
        val environmentService = mock<EnvironmentService> {
            on { isDev() }.thenReturn(true)
        }

        val config = ConfigFactory.parseString(configString)
        val service = ClientAuthenticationService(
            environmentService,
            config
        )

        val apiKey = service.authenticateClient(clientName, clientPassword)
        then(apiKey).isNotBlank()
        then(apiKey!!.length)
            .withFailMessage("API key is too weak")
            .isGreaterThanOrEqualTo(32)
    }

    @Test
    fun `should deauthenticate client`() {
        val environmentService = mock<EnvironmentService> {
            on { isDev() }.thenReturn(true)
        }

        val config = ConfigFactory.parseString(configString)
        val service = ClientAuthenticationService(
            environmentService,
            config
        )

        val apiKey = service.authenticateClient(clientName, clientPassword)!!
        then(service.deauthenticateClient(apiKey)).isTrue()
    }

    @Test
    fun `should find authenticated client`() {
        val environmentService = mock<EnvironmentService> {
            on { isDev() }.thenReturn(true)
        }

        val config = ConfigFactory.parseString(configString)
        val service = ClientAuthenticationService(
            environmentService,
            config
        )

        val key = service.authenticateClient(clientName, clientPassword)!!
        then(service.getAuthenticatedClientName(key)).isEqualTo(clientName)
    }

    @Test
    fun `should impose password length`() {
        val environmentService = mock<EnvironmentService> {
            on { isDev() }.thenReturn(true)
        }

        val configString = """
            terminal {
              clients {
                registered = [
                  {
                    name = test-name
                    password = short
                  }
                ]
              }
            }
        """.trimIndent()
        val config = ConfigFactory.parseString(configString)

        assertThatIllegalStateException().isThrownBy {
            ClientAuthenticationService(
                environmentService,
                config
            )
        }
    }
}