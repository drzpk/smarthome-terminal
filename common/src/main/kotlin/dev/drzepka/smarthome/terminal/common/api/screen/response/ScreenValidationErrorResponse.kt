package dev.drzepka.smarthome.terminal.common.api.screen.response

class ScreenValidationErrorResponse : ProcessScreenUpdateResponse() {
    override val status = Status.ERROR
    var errors: Map<Int, String>? = null
}