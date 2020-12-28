package dev.drzepka.smarthome.terminal.server.application.configuration.feature

import dev.drzepka.smarthome.terminal.server.application.data.ErrorResponse
import dev.drzepka.smarthome.terminal.server.application.exception.AccessForbiddenException
import dev.drzepka.smarthome.terminal.server.application.exception.NotFoundException
import dev.drzepka.smarthome.terminal.server.domain.exception.IdentifiableException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import org.slf4j.LoggerFactory

object StatusPages {

    private val log = LoggerFactory.getLogger("ExceptionHandler")

    fun install(application: Application) {
        application.install(io.ktor.features.StatusPages) {
            exception<Throwable> { cause ->
                val errorResponse = getErrorResponse(cause, call)
                val statusCode = HttpStatusCode.fromValue(errorResponse.httpStatus)
                call.respond(statusCode, errorResponse)
            }
        }
    }

    private fun getErrorResponse(exception: Throwable, call: ApplicationCall): ErrorResponse {
        return if (exception is IdentifiableException) {
            val status = getStatusCode(exception)
            ErrorResponse(ErrorResponse.Type.ERROR, status, exception.code, exception.message)
        } else {
            log.error("Unknown error while processing ${call.request.uri}", exception)
            val status = HttpStatusCode.InternalServerError.value
            // Don't reveal the message for security reasons
            ErrorResponse(ErrorResponse.Type.ERROR, status, null, "Unknown error")
        }
    }

    private fun getStatusCode(exception: IdentifiableException): Int {
        val status = when (exception) {
            is AccessForbiddenException -> HttpStatusCode.Forbidden
            is NotFoundException -> HttpStatusCode.NotFound
            else -> HttpStatusCode.InternalServerError
        }

        return status.value
    }
}