package dev.drzepka.smarthome.terminal.common.api.clients

class RegisterClientRequest

data class RegisterClientResponse(
    var status: Boolean,
    var clientId: Int?,
    var message: String?
)

class UnregisterClientRequest

class UnregisterClientResponse