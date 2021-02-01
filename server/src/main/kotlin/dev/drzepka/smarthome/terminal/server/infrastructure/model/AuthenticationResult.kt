package dev.drzepka.smarthome.terminal.server.infrastructure.model

data class AuthenticationResult(
    val result: Boolean,
    val clientId: String
)