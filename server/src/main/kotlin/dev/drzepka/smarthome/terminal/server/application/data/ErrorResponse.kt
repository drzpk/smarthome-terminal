package dev.drzepka.smarthome.terminal.server.application.data

data class ErrorResponse(val type: Type, val httpStatus: Int, val code: String?, val message: String?) {
    enum class Type {
        ERROR
    }
}