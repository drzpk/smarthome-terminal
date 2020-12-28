package dev.drzepka.smarthome.terminal.server.application.data

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

data class ApiClientPrincipal(override val name: String, val client: Client?) : TerminalPrincipal() {
    override val authenticated: Boolean
        get() = client != null
}

class WebClientPrincipal(override val name: String) : TerminalPrincipal() { // TODO: web sessions
    override val authenticated: Boolean = false
}