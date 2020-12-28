package dev.drzepka.smarthome.terminal.server.application.configuration.feature

import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.application.data.PrincipalContainer
import dev.drzepka.smarthome.terminal.server.application.service.ClientAuthenticationService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.request.*
import org.koin.ktor.ext.get

object Security {

    const val SERVER_AUTH_NAME = "serverAuthentication"

    private val log by Logger()

    fun install(application: Application) {
        val clientAuthenticationService = application.get<ClientAuthenticationService>()

        application.install(Authentication) {
            basic(SERVER_AUTH_NAME) {
                realm = "Terminal Server"
                validate { credentials ->
                    val principal = clientAuthenticationService.getPrincipal(credentials)
                    if (principal == null)
                        notifyUnauthorizedAccess(request)
                    PrincipalContainer(principal!!)
                }
            }
        }
    }

    private fun notifyUnauthorizedAccess(request: ApplicationRequest) {
        // todo: block access after X trials
        log.warn("Unauthorized access from {} at {}", request.origin.remoteHost, request.uri)
    }
}
