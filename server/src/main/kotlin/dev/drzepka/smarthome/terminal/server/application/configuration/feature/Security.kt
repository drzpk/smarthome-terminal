package dev.drzepka.smarthome.terminal.server.application.configuration.feature

import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.application.service.ClientIdentityService
import dev.drzepka.smarthome.terminal.server.infrastructure.security.PrincipalContainer
import dev.drzepka.smarthome.terminal.server.infrastructure.security.apiKey
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.request.*
import org.koin.ktor.ext.get

object Security {

    const val AUTH_PROVIDER_CLIENT_API = "client_api"

    private val log by Logger()

    fun install(application: Application) {
        val clientIdentityService = application.get<ClientIdentityService>()

        application.install(Authentication) {
            apiKey(AUTH_PROVIDER_CLIENT_API) {
                validate { credentials ->
                    val principal = clientIdentityService.getPrincipal(credentials)
                    if (principal == null)
                        notifyUnauthorizedAccess(request)

                    principal?.let { PrincipalContainer(it) }
                }
            }
        }
    }

    private fun notifyUnauthorizedAccess(request: ApplicationRequest) {
        // todo: block access after X trials
        log.warn("Unauthorized access from {} at {}", request.origin.remoteHost, request.uri)
    }
}
