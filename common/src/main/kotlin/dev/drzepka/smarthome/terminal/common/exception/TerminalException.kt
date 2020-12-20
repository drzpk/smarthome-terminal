package dev.drzepka.smarthome.terminal.common.exception

abstract class TerminalException : RuntimeException {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)
}