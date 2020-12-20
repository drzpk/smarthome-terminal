package dev.drzepka.smarthome.terminal.common.transport.message

import dev.drzepka.smarthome.terminal.common.api.element.ScreenModel
import dev.drzepka.smarthome.terminal.common.transport.Side

/**
 * Sent when a screen is requested by an end user.
 */
class GetScreenMessage : Message<GetScreenMessageResponse>(Side.CLIENT, GetScreenMessageResponse::class) {
    var categoryId = 0
}

class GetScreenMessageResponse : MessageResponse {
    @Suppress("unused")
    constructor() : super()

    constructor(message: GetScreenMessage) : super(message)

    /**
     * Requested screen or null if screen with given id wasn't found.
     */
    var screen: ScreenModel? = null
}