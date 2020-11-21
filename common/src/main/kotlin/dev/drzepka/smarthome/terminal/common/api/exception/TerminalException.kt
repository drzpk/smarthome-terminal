package dev.drzepka.smarthome.terminal.common.api.exception

import java.lang.RuntimeException

abstract class TerminalException : RuntimeException {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)
}