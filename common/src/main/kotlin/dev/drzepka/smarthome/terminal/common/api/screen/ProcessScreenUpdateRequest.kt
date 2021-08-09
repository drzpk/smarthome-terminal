package dev.drzepka.smarthome.terminal.common.api.screen

class ProcessScreenUpdateRequest {
    var screenId: Int = 0
    var properties = emptyMap<Int, String?>()
}