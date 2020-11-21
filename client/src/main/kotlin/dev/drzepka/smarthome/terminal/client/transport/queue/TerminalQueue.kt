package dev.drzepka.smarthome.terminal.client.transport.queue

import dev.drzepka.smarthome.terminal.common.transport.message.Message
import dev.drzepka.smarthome.terminal.common.transport.message.MessageResponse

interface TerminalQueue {
    var handler: Handler?

    suspend fun <Response : MessageResponse> putMessage(message: Message<Response>): Response

    interface Handler {
        fun <Response : MessageResponse> processMessage(message: Message<Response>): Response
    }
}