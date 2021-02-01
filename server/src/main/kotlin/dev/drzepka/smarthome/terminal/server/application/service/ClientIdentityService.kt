package dev.drzepka.smarthome.terminal.server.application.service

import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.domain.service.client.ClientService
import dev.drzepka.smarthome.terminal.server.infrastructure.security.ApiClientPrincipal
import dev.drzepka.smarthome.terminal.server.infrastructure.security.ApiKeyCredential
import dev.drzepka.smarthome.terminal.server.infrastructure.security.TerminalPrincipal
import dev.drzepka.smarthome.terminal.server.infrastructure.service.ClientAuthenticationService

class ClientIdentityService(
    private val clientService: ClientService,
    private val clientAuthenticationService: ClientAuthenticationService
) {
    private val log by Logger()

    /**
     * Returns principal if provided credentials are correct, null otherwise.
     */
    fun authenticateApiClient(clientName: String, password: String): ApiClientPrincipal? {
        log.debug("Authenticating api client '{}'", clientName)

        var apiKey: String? = null
        try {
            apiKey = clientAuthenticationService.authenticateClient(clientName, password) ?: return null
            val client = clientService.registerClient(clientName)
            log.debug("API client '{}' successfully authenticated", clientName)
            return ApiClientPrincipal(client, apiKey)
        } catch (e: Exception) {
            log.debug("An exception occurred while authenticating api client, reverting changes")
            if (apiKey != null)
                clientAuthenticationService.deauthenticateClient(apiKey)
            throw e
        }
    }

    fun deauthenticateApiClient(principal: ApiClientPrincipal) {
        log.debug("Deauthenticating api client '{}'", principal.client?.name)
        if (principal.client != null)
            clientService.unregisterClient(principal.client)
        clientAuthenticationService.deauthenticateClient(principal.apiKey)
    }

    suspend fun getPrincipal(credentials: ApiKeyCredential): TerminalPrincipal? {
        val clientName = clientAuthenticationService.getAuthenticatedClientName(credentials.apiKey) ?: return null
        val client = clientService.findClient(clientName, false)
        if (client == null) {
            log.debug("Client wasn't found for name {}, deauthenticating", clientName)
            clientAuthenticationService.deauthenticateClient(credentials.apiKey)
            return null
        }

        return ApiClientPrincipal(client, credentials.apiKey)
    }
}