package dev.drzepka.smarthome.terminal.server.application.configuration.routes

import dev.drzepka.smarthome.terminal.common.api.clients.RegisterClientResponse
import dev.drzepka.smarthome.terminal.server.application.exception.NotFoundException
import dev.drzepka.smarthome.terminal.server.application.utils.getApiClientPrincipal
import dev.drzepka.smarthome.terminal.server.domain.service.client.ClientService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get
import org.slf4j.LoggerFactory

fun Route.clients() {
    val log = LoggerFactory.getLogger("ClientsRoute")

    route("/clients") {
        val clientService = get<ClientService>()

        post("/register") {
            val principal = getApiClientPrincipal()!!

            val response = try {
                val client = clientService.registerClient(principal.name)
                RegisterClientResponse(true, client.id, "success")
            } catch (e: Exception) {
                log.error("Error while registering client with name='{}'", principal.name, e)
                RegisterClientResponse(false, null, e.message)
            }
            call.respond(response)
        }

        post("/unregister") {
            val principal = getApiClientPrincipal()!!
            if (principal.client != null) {
                clientService.unregisterClient(principal.client)
                call.respond(HttpStatusCode.NoContent)
            } else {
                throw NotFoundException("No client was registered")
            }
        }
    }
}