package dev.drzepka.smarthome.terminal.server.application.exception

import dev.drzepka.smarthome.terminal.server.domain.exception.IdentifiableException

class AccessForbiddenException : IdentifiableException {
    override val code = "forbidden"

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}

class NotFoundException : IdentifiableException {
    override val code = "not.found"

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}