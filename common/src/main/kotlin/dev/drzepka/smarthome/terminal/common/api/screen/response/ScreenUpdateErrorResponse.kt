package dev.drzepka.smarthome.terminal.common.api.screen.response

class ScreenUpdateErrorResponse : ProcessScreenUpdateResponse() {
    override val status = Status.ERROR
    var message: String? = null
}