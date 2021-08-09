package dev.drzepka.smarthome.terminal.common.api.screen.response

class ScreenUpdatedResponse : ProcessScreenUpdateResponse() {
    override val status = Status.UPDATED
    var message: String? = null
}