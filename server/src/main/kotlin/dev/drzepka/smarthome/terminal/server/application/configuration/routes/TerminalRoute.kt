package dev.drzepka.smarthome.terminal.server.application.configuration.routes

import dev.drzepka.smarthome.terminal.common.transport.message.Message
import dev.drzepka.smarthome.terminal.common.transport.message.MessageResponse
import dev.drzepka.smarthome.terminal.server.application.utils.requireAuthorizedPrincipal
import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import dev.drzepka.smarthome.terminal.server.domain.repository.ClientRepository
import dev.drzepka.smarthome.terminal.server.domain.service.TerminalQueue
import dev.drzepka.smarthome.terminal.server.infrastructure.service.TerminalQueueImpl
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get

fun Route.terminal() {
    val queue = get<TerminalQueue>() as TerminalQueueImpl
    val userRepository = get<ClientRepository>()
    val testClient = Client(1, "test client")

    fun getClient(): Client {
        // fixme: debug
        return userRepository.findAll().firstOrNull() ?: testClient
    }

    route("/terminal") {
        requireAuthorizedPrincipal()

        post("/queue") {
            val message = call.receive<Message<out MessageResponse>>()
            // todo: what if client sends message with target side: client?
            val reply = queue.putMessage(getClient(), message)
            call.respond(reply)
        }

        get("/queue/poll") {
            val messages = queue.getQueuedMessages(getClient())
            call.respond(messages)
        }

        put("/queue/poll") {
            val reply = call.receive<MessageResponse>()
            queue.provideResponses(getClient(), listOf(reply))
            call.respondText("OK")
        }
    }
}