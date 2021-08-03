package dev.drzepka.smarthome.terminal.server.application.configuration.feature

import dev.drzepka.smarthome.terminal.common.configurer.JacksonConfigurer
import dev.drzepka.smarthome.terminal.server.application.configuration.koinModule
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*

fun Application.setupFeatures() {
    install(org.koin.ktor.ext.Koin) {
        modules(koinModule)
    }

    Security.install(this)
    StatusPages.install(this)

    // Web
    install(ContentNegotiation) {
        jackson {
            JacksonConfigurer.configure(this)
        }
    }
}
