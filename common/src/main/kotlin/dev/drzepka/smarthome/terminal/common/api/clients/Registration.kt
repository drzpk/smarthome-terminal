package dev.drzepka.smarthome.terminal.common.api.clients

data class RegisterClientRequest(
    var name: String?,
    var password: String?
)

data class RegisterClientResponse(
    var clientId: Int,
    var apiKey: String
)

class UnregisterClientRequest

class UnregisterClientResponse