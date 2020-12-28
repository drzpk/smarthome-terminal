package dev.drzepka.smarthome.terminal.server.domain.exception

/**
 * Exception that can
 */
abstract class IdentifiableException : RuntimeException {
    abstract val code: String

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}

