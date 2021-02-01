package dev.drzepka.smarthome.terminal.server.infrastructure.service

import com.typesafe.config.Config
import com.typesafe.config.ConfigBeanFactory
import com.typesafe.config.ConfigFactory
import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.application.properties.ClientProperties
import dev.drzepka.smarthome.terminal.server.application.properties.ClientsProperties
import dev.drzepka.smarthome.terminal.server.domain.service.EnvironmentService
import dev.drzepka.smarthome.terminal.server.infrastructure.exception.ApiKeyExpiredException
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.absoluteValue

class ClientAuthenticationService(
    private val environmentService: EnvironmentService,
    private val applicationConfig: Config = ConfigFactory.load()
) {

    private val log by Logger()
    private val clientsCredentials = HashMap<String, String>()
    private val authenticatedClients = ConcurrentHashMap<String, KeyData>()

    init {
        loadClientsConfiguration()
    }

    /**
     * Auuthenticates client and returns API key. If client is already authenticated, the old API key is discarted.
     * @return API key if credentials are correct, null otherwise
     */
    fun authenticateClient(name: String, password: String): String? {
        if (!areCredentialsValid(name, password))
            return null

        val key = generateApiKey()
        val data = KeyData(
            key,
            name,
            LocalDateTime.now().plusSeconds(API_KEY_LIFESPAN_SECONDS.toLong())
        )

        authenticatedClients[key] = data
        return key
    }

    /**
     * Deauthenticates client.
     * @return whether client existed and was deauthenticated.
     */
    fun deauthenticateClient(apiKey: String): Boolean {
        return authenticatedClients.remove(apiKey) != null
    }

    fun getAuthenticatedClientName(apiKey: String): String? {
        val data = authenticatedClients[apiKey]
        if (data?.isExpired() == true) {
            deauthenticateClient(apiKey)
            throw ApiKeyExpiredException(data.clientName)
        }

        return data?.clientName
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

        log.info(
            "Loading test client '{}'",
            TEST_CLIENT_NAME
        )
        val properties = ClientProperties().apply {
            name =
                TEST_CLIENT_NAME
            password =
                TEST_CLIENT_PASSWORD
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

    private fun areCredentialsValid(name: String, password: String): Boolean {
        return clientsCredentials[name] == password
    }

    private fun generateApiKey(): String {
        return API_KEY_RANDOM.ints(API_KEY_LENGTH.toLong())
            .mapToObj { API_KEY_CHARSET[it.absoluteValue % API_KEY_CHARSET.length].toString() }
            .reduce("") { l, r -> l + r }
    }

    private data class KeyData(val apiKey: String, val clientName: String, val expiresAt: LocalDateTime) {
        fun isExpired(): Boolean = expiresAt.isBefore(LocalDateTime.now())
    }

    companion object {
        private const val TEST_CLIENT_NAME = "test"
        private const val TEST_CLIENT_PASSWORD = "test-client-password-123"

        private const val API_KEY_LIFESPAN_SECONDS = 6 * 60 * 60
        private const val API_KEY_CHARSET = "0123456789abcdef"
        private const val API_KEY_LENGTH = 64
        private val API_KEY_RANDOM = SecureRandom()
    }
}