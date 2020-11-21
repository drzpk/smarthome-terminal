package dev.drzepka.smarthome.terminal.server.domain.service

import dev.drzepka.smarthome.terminal.common.transport.message.Message
import dev.drzepka.smarthome.terminal.common.transport.message.MessageResponse
import dev.drzepka.smarthome.terminal.server.domain.entity.Client

/**
 * Means of communication between the terminal server and connected clients.
 */
interface TerminalQueue {
    var handler: Handler?

    suspend fun <Response : MessageResponse> putMessage(client: Client, message: Message<Response>): Response

    interface Handler {
        fun <Response : MessageResponse> processMessage(client: Client, message: Message<Response>): Response
    }
}