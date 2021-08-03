package dev.drzepka.smarthome.terminal.common.transport.message

import dev.drzepka.smarthome.terminal.common.transport.Side

/**
 * Sent when apply button is clieked in the terminal.
 */
class ScreenUpdateMessage : Message<ScreenUpdateMessageResponse>(Side.CLIENT, ScreenUpdateMessageResponse::class) {
    var screenId = 0
    var propertyValues = emptyMap<Int, String?>()
}

class ScreenUpdateMessageResponse : MessageResponse {
    @Suppress("unused")
    constructor() : super()

    constructor(message: ScreenUpdateMessage) : super(message)

    /** How message was handled by the client. */
    var status: Status = Status.UNKNOWN
    /** Detailed message about status (i.e. error message). */
    var message: String? = null

    enum class Status {
        UPDATED, ERROR, UNKNOWN
    }
}