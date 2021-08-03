package dev.drzepka.smarthome.terminal.common.api.screen

class ProcessScreenUpdateRequest {
    var screenId: Int = 0
    var properties = emptyMap<Int, String?>()
}

class ProcessScreenUpdateResponse {
    var status = Status.ERROR
    var message: String? = null
    var errors: Map<Int, String>? = null

    @Suppress("unused")
    enum class Status {
        UPDATED, ERROR, UNKNOWN, VALIDATION_ERROR
    }
}