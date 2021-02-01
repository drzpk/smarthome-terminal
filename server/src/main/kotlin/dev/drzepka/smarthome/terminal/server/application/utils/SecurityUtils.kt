package dev.drzepka.smarthome.terminal.server.application.utils

import dev.drzepka.smarthome.terminal.server.application.exception.AccessForbiddenException
import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import dev.drzepka.smarthome.terminal.server.infrastructure.security.ApiClientPrincipal
import dev.drzepka.smarthome.terminal.server.infrastructure.security.PrincipalContainer
import dev.drzepka.smarthome.terminal.server.infrastructure.security.TerminalPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun PipelineContext<*, ApplicationCall>.getPrincipal(): TerminalPrincipal? {
    val container = call.authentication.principal<PrincipalContainer>()
    return container?.principal
}

fun PipelineContext<*, ApplicationCall>.getApiClientPrincipal(): ApiClientPrincipal? {
    return getPrincipal() as ApiClientPrincipal?
}

fun PipelineContext<*, ApplicationCall>.getRequestClient(): Client? {
    val principal = getPrincipal()
    if (principal is ApiClientPrincipal)
        return principal.client
    else
        throw IllegalStateException("Principal is not an ApiClientPrincipal")
}

fun Route.requireAuthorizedPrincipal() {
    intercept(ApplicationCallPipeline.Call) {
        if (getPrincipal()?.authenticated != true) {
            throw AccessForbiddenException("Client is not authorized to use this resource")
        }
    }
}