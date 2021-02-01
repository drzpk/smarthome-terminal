package dev.drzepka.smarthome.terminal.server.application.configuration.routes

import dev.drzepka.smarthome.terminal.common.transport.message.Message
import dev.drzepka.smarthome.terminal.common.transport.message.MessageResponse
import dev.drzepka.smarthome.terminal.server.application.utils.getApiClientPrincipal
import dev.drzepka.smarthome.terminal.server.application.utils.requireAuthorizedPrincipal
import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import dev.drzepka.smarthome.terminal.server.domain.service.TerminalQueue
import dev.drzepka.smarthome.terminal.server.infrastructure.service.TerminalQueueImpl
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.get

fun Route.terminal() {
    val queue = get<TerminalQueue>() as TerminalQueueImpl

    route("/terminal") {
        requireAuthorizedPrincipal()

        fun PipelineContext<*, ApplicationCall>.getClient(): Client {
            val client = getApiClientPrincipal()!!.client!!
            // Queue endpoints act as a ping message
            client.notifyActive()
            return client
        }

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