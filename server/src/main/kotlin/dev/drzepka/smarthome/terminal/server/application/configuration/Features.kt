package dev.drzepka.smarthome.terminal.server.application.configuration

import dev.drzepka.smarthome.terminal.common.configurer.JacksonConfigurer
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.jackson
import io.ktor.request.*
import io.ktor.response.*
import org.slf4j.LoggerFactory

fun Application.setupFeatures() {
    install(org.koin.ktor.ext.Koin) {
        modules(koinModule)
    }

    // Web
    install(ContentNegotiation) {
        jackson {
            JacksonConfigurer.configure(this)
        }
    }
    install(CORS) {
        anyHost() // todo: debug mode only
    }
    install(StatusPages) {
        val log = LoggerFactory.getLogger("ExceptionHandler")
        exception<Throwable> { cause ->
            log.error("Error while processing ${call.request.uri}", cause)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
