package dev.drzepka.smarthome.terminal.server.infrastructure.security

import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import io.ktor.auth.*

/**
 * Wrapper class is required for principal because KTOR matches them by the exact type.
 */
data class PrincipalContainer(val principal: TerminalPrincipal) : Principal

sealed class TerminalPrincipal : Principal {
    abstract val name: String
    abstract val authenticated: Boolean
}

data class ApiClientPrincipal(val client: Client?, val apiKey: String) : TerminalPrincipal() {
    override val name: String = client!!.name
    override val authenticated: Boolean = client != null
}

class WebClientPrincipal(override val name: String) : TerminalPrincipal() { // TODO: web sessions
    override val authenticated: Boolean = false
}