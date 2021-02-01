package dev.drzepka.smarthome.terminal.server.application.utils

import dev.drzepka.smarthome.terminal.common.validation.ObjectValidator
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.util.pipeline.*

suspend inline fun <reified T : Any> PipelineContext<*, ApplicationCall>.getRequestBody(): T {
    val body = call.receive<T>()
    ObjectValidator.validate(body)
    return body
}