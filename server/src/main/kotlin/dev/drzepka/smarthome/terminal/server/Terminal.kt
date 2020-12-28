package dev.drzepka.smarthome.terminal.server

import dev.drzepka.smarthome.terminal.server.application.configuration.feature.setupFeatures
import dev.drzepka.smarthome.terminal.server.application.configuration.setupRouting
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun Application.terminalServer() {
    setupFeatures()
    setupRouting()
}

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8081) {
        terminalServer()
    }

    server.start(wait = true)
}
