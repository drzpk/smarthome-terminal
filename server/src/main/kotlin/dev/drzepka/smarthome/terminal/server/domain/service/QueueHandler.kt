package dev.drzepka.smarthome.terminal.server.domain.service

import dev.drzepka.smarthome.terminal.common.transport.message.Message
import dev.drzepka.smarthome.terminal.common.transport.message.MessageResponse
import dev.drzepka.smarthome.terminal.server.domain.entity.Client

class QueueHandler : TerminalQueue.Handler {

    override fun <Response : MessageResponse> processMessage(client: Client, message: Message<Response>): Response {
        TODO("Not yet implemented")
    }
}