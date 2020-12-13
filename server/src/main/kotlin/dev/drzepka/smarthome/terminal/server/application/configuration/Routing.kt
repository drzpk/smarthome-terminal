package dev.drzepka.smarthome.terminal.server.application.configuration

import dev.drzepka.smarthome.terminal.server.application.configuration.routes.applications
import dev.drzepka.smarthome.terminal.server.application.configuration.routes.clients
import dev.drzepka.smarthome.terminal.server.application.configuration.routes.terminal
import io.ktor.application.*
import io.ktor.routing.*

fun Application.setupRouting() {
    routing {
        route("/api") {
            applications()
            clients()
            terminal()
        }
    }
}