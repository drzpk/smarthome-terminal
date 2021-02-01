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

//class ValidationException : IdentifiableException { // todo: validation
//    override val code = "validation"
//    val errors = ArrayList<FieldError>()
//
//    constructor() : super("Request validation failed")
//
//    fun addFieldError(fieldName: String, errorMessage: String) {
//        errors.add(FieldError(fieldName, errorMessage))
//    }
//
//    data class FieldError(val fieldName: String, val errorMessage: String)
//}