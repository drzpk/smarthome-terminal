package dev.drzepka.smarthome.terminal.server.application.service

import com.typesafe.config.Config
import com.typesafe.config.ConfigBeanFactory
import com.typesafe.config.ConfigFactory
import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.application.data.ApiClientPrincipal
import dev.drzepka.smarthome.terminal.server.application.data.TerminalPrincipal
import dev.drzepka.smarthome.terminal.server.application.properties.ClientProperties
import dev.drzepka.smarthome.terminal.server.application.properties.ClientsProperties
import dev.drzepka.smarthome.terminal.server.domain.service.EnvironmentService
import dev.drzepka.smarthome.terminal.server.domain.service.client.ClientService
import io.ktor.auth.*

class ClientAuthenticationService(
    private val clientService: ClientService,
    private val environmentService: EnvironmentService,
    private val applicationConfig: Config = ConfigFactory.load()
) {

    private val log by Logger()
    private val clientsCredentials = HashMap<String, String>()

    init {
        loadClientsConfiguration()
    }

    suspend fun getPrincipal(credentials: UserPasswordCredential): TerminalPrincipal? {
        val password = clientsCredentials[credentials.name]
        if (password == null) {
            log.error("No client credentials found for name '{}'", credentials.name)
            return null
        } else if (password != credentials.password) {
            log.error("Wrong password for client credentials with name '{}'", credentials.name)
            return null
        }

        val client = clientService.findClient(credentials.name, false)
        return ApiClientPrincipal(credentials.name, client)
    }

    private fun loadClientsConfiguration() {
        log.info("Initializing clients configuration")
        val clientsConfig = applicationConfig.getConfig("terminal.clients")
        val clientsProperties = ConfigBeanFactory.create(clientsConfig, ClientsProperties::class.java)!!

        var loaded = 0
        clientsProperties.registered.forEach {
            try {
                log.trace("Initailizing client '{}'", it.name)
                loadClientCredentials(it)
                loaded++
            } catch (e: Exception) {
                throw IllegalStateException("Error while loading client properties at position $loaded", e)
            }
        }

        loadTestClientCredentials()
        log.info("Initialized {} client(s)", loaded)
    }

    private fun loadTestClientCredentials() {
        if (!environmentService.isDev())
            return

        log.info("Loading test client '{}'", TEST_CLIENT_NAME)
        val properties = ClientProperties().apply {
            name = TEST_CLIENT_NAME
            password = TEST_CLIENT_PASSWORD
        }
        loadClientCredentials(properties)
    }

    private fun loadClientCredentials(properties: ClientProperties) {
        @Suppress("CascadeIf")
        if (properties.name.isBlank())
            throw IllegalArgumentException("Client name cannot be blank")
        else if (clientsCredentials.containsKey(properties.name))
            throw IllegalArgumentException("Client with name='${properties.name}' already exists")
        else if (properties.password.length < 8)
            throw IllegalArgumentException("Client password must have at least 8 characters")

        clientsCredentials[properties.name] = properties.password
    }

    companion object {
        private const val TEST_CLIENT_NAME = "test"
        private const val TEST_CLIENT_PASSWORD = "test-client-password-123"
    }
}