package dev.drzepka.smarthome.terminal.common.api.clients

data class RegisterClientRequest(
    var clientName: String,
    var clientSecret: String
)

data class RegisterClientResponse(
    var status: Boolean,
    var clientId: Int?,
    var message: String?,
    var bearerToken: String?
)

class UnregisterClientRequest

class UnregisterClientResponse