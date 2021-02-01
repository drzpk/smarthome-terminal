package dev.drzepka.smarthome.terminal.server.infrastructure.exception

import dev.drzepka.smarthome.terminal.server.domain.exception.IdentifiableException

class ApiKeyExpiredException(clientName: String) : IdentifiableException("API key for client '$clientName' expired.") {
    override val code: String = "api.key.expired"

}