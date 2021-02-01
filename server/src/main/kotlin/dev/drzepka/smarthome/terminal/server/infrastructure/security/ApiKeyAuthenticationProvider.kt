package dev.drzepka.smarthome.terminal.server.infrastructure.security

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.auth.*
import io.ktor.response.*

class ApiKeyCredential(val apiKey: String) : Credential

class ApiKeyAuthenticationProviderConfiguration(name: String) : AuthenticationProvider.Configuration(name) {

    var authenticationFunction: (suspend ApplicationCall.(ApiKeyCredential) -> PrincipalContainer?)? = null

    fun validate(body: suspend ApplicationCall.(ApiKeyCredential) -> PrincipalContainer?) {
        authenticationFunction = body
    }
}

class ApiKeyAuthenticationProvider(config: ApiKeyAuthenticationProviderConfiguration) : AuthenticationProvider(config) {
    val authenticationFunction = config.authenticationFunction
}

fun Authentication.Configuration.apiKey(
    name: String,
    configure: ApiKeyAuthenticationProviderConfiguration.() -> Unit
) {
    val provider = ApiKeyAuthenticationProvider(ApiKeyAuthenticationProviderConfiguration(name).apply(configure))
    val authFunction = provider.authenticationFunction

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        var authFailureCause: AuthenticationFailedCause? = null

        val authHeader = call.request.headers["Authorization"]
        if (authHeader != null) {
            val parsedHeader = parseAuthorizationHeader(authHeader)
            if (parsedHeader?.authScheme?.equals("bearer", ignoreCase = true) != true) {
                authFailureCause = AuthenticationFailedCause.InvalidCredentials
            }

            val key = (parsedHeader as HttpAuthHeader.Single?)?.blob
            val principal = key?.let { authFunction?.invoke(call, ApiKeyCredential(it)) }
            if (principal != null)
                context.principal(principal)
            else
                authFailureCause = AuthenticationFailedCause.InvalidCredentials

        } else {
            authFailureCause = AuthenticationFailedCause.NoCredentials
        }

        if (authFailureCause != null) {
            context.challenge("apiKeyProvider", authFailureCause) {
                val response =
                    if (authFailureCause is AuthenticationFailedCause.NoCredentials) UnauthorizedResponse() else ForbiddenResponse()
                call.respond(response)
                it.complete()
            }
        }
    }

    register(provider)
}