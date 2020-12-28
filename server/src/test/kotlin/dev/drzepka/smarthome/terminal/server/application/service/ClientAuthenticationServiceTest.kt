package dev.drzepka.smarthome.terminal.server.application.service

import com.nhaarman.mockitokotlin2.mock
import com.typesafe.config.ConfigFactory
import dev.drzepka.smarthome.terminal.server.application.data.ApiClientPrincipal
import dev.drzepka.smarthome.terminal.server.domain.service.EnvironmentService
import dev.drzepka.smarthome.terminal.server.domain.service.client.ClientService
import dev.drzepka.smarthome.terminal.server.domain.service.client.ClientServiceImpl
import io.ktor.auth.*
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.BDDAssertions.assertThatIllegalStateException
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class ClientAuthenticationServiceTest {

    @Test
    fun `should load clients configuration`() = runBlockingTest {
        val environmentService = mock<EnvironmentService> {
            on { isDev() }.thenReturn(true)
        }

        val principalName = "name"
        val principalPassword = "password123456789"
        val configString = """
            terminal {
              clients {
                registered = [
                  {
                    name = $principalName
                    password = $principalPassword
                  }
                ]
              }
            }
        """.trimIndent()
        val clientService = mock<ClientService> {}
        val config = ConfigFactory.parseString(configString)
        val service = ClientAuthenticationService(clientService, environmentService, config)

        val principal = service.getPrincipal(UserPasswordCredential(principalName, principalPassword))
        then(principal).isInstanceOf(ApiClientPrincipal::class.java)
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
        val clientService = mock<ClientServiceImpl> {}
        val config = ConfigFactory.parseString(configString)

        assertThatIllegalStateException().isThrownBy {
            ClientAuthenticationService(clientService, environmentService, config)
        }
    }
}