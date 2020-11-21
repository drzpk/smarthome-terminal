package dev.drzepka.smarthome.terminal.server.application.configuration.routes

import dev.drzepka.smarthome.terminal.common.api.clients.RegisterClientRequest
import dev.drzepka.smarthome.terminal.common.api.clients.RegisterClientResponse
import dev.drzepka.smarthome.terminal.common.api.clients.UnregisterClientResponse
import dev.drzepka.smarthome.terminal.server.domain.repository.ClientRepository
import dev.drzepka.smarthome.terminal.server.domain.service.ClientService
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject

fun Route.clients() {
    route("/clients") {
        val clientService = get<ClientService>()
        val clientRepository by inject<ClientRepository>()

        // todo: implement session

        post("/register") {
            val request = call.receive<RegisterClientRequest>()
            clientService.registerClient(request.clientName, request.clientSecret)
            val response = RegisterClientResponse(true, "success", "not implemented yet")
            call.respond(response)
        }

        post("/unregister") {
            clientRepository.findAll()
                .forEach { clientService.unregisterClient(it) }
            call.respond(UnregisterClientResponse())
        }
    }
}