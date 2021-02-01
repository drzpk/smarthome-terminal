package dev.drzepka.smarthome.terminal.server.application.configuration.routes

import dev.drzepka.smarthome.terminal.common.api.clients.RegisterClientRequest
import dev.drzepka.smarthome.terminal.common.api.clients.RegisterClientResponse
import dev.drzepka.smarthome.terminal.server.application.configuration.feature.Security
import dev.drzepka.smarthome.terminal.server.application.exception.AccessForbiddenException
import dev.drzepka.smarthome.terminal.server.application.service.ClientIdentityService
import dev.drzepka.smarthome.terminal.server.application.utils.getApiClientPrincipal
import dev.drzepka.smarthome.terminal.server.application.utils.getRequestBody
import dev.drzepka.smarthome.terminal.server.domain.exception.IdentifiableException
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get
import org.slf4j.LoggerFactory

fun Route.clients() {
    val log = LoggerFactory.getLogger("ClientsRoute")

    route("/clients") {
        val clientIdentityService = get<ClientIdentityService>()

        post("/register") {
            val request = getRequestBody<RegisterClientRequest>()
            val response = try {
                val principal = clientIdentityService.authenticateApiClient(request.name!!, request.password!!)
                if (principal != null)
                    RegisterClientResponse(principal.client!!.id, principal.apiKey)
                else
                    null
            } catch (e: Exception) {
                log.error("Error while registering client with name='{}'", request.name, e)
                if (e is IdentifiableException)
                    throw e
                else
                    throw AccessForbiddenException("Error during client registration")
            }
                ?: throw AccessForbiddenException("Invalid credentials")

            call.respond(response)
        }

        authenticate(Security.AUTH_PROVIDER_CLIENT_API) {
            post("/unregister") {
                val principal = getApiClientPrincipal()!!
                clientIdentityService.deauthenticateApiClient(principal)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}