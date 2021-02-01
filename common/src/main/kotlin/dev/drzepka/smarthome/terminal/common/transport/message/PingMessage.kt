package dev.drzepka.smarthome.terminal.common.transport.message

import dev.drzepka.smarthome.terminal.common.transport.Side

// Ping messages are used for testing

class PingClientMessage : Message<PingClientMessageResponse>(Side.CLIENT, PingClientMessageResponse::class)

class PingClientMessageResponse : MessageResponse {
    @Suppress("unused")
    constructor() : super()
    constructor(message: PingClientMessage) : super(message)
}

class PingServerMessage : Message<PingServerMessageResponse>(Side.SERVER, PingServerMessageResponse::class)

class PingServerMessageResponse : MessageResponse {
    @Suppress("unused")
    constructor() : super()
    constructor(message: PingServerMessage) : super(message)
}
