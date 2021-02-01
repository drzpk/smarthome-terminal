package dev.drzepka.smarthome.terminal.server.application.configuration

import dev.drzepka.smarthome.terminal.server.application.configuration.feature.Security
import dev.drzepka.smarthome.terminal.server.application.configuration.routes.applications
import dev.drzepka.smarthome.terminal.server.application.configuration.routes.clients
import dev.drzepka.smarthome.terminal.server.application.configuration.routes.terminal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*

fun Application.setupRouting() {
    routing {
        route("/api") {
            clients()
            authenticate(Security.AUTH_PROVIDER_CLIENT_API) {
                terminal()
            }
        }

        applications()
    }
}