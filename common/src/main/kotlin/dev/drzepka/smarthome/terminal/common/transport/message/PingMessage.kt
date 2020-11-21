package dev.drzepka.smarthome.terminal.common.transport.message

import dev.drzepka.smarthome.terminal.common.transport.Side

class PingMessage : Message<PingMessageResponse>(Side.CLIENT, PingMessageResponse::class)

class PingMessageResponse : MessageResponse {
    @Suppress("unused")
    constructor() : super()
    constructor(message: PingMessage) : super(message)
}
