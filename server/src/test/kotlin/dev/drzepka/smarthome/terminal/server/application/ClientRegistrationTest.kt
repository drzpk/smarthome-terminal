package dev.drzepka.smarthome.terminal.server.application

import dev.drzepka.smarthome.terminal.server.terminalServer
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class ClientRegistrationTest {

    @Test
    fun `should register and unregister client`() = withTestApplication(Application::terminalServer) {
        handleRequest(HttpMethod.Post, "/api/clients/register") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{ "clientName": "testName", "clientSecret": "secret" }""")
        }.apply {
            val result = response.status()!!.value
            then(result).isEqualTo(200)
        }

        handleRequest(HttpMethod.Post, "/api/clients/unregister") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{ "clientName": "testName", "clientSecret": "secret" }""")
        }.apply {
            val result = response.status()!!.value
            then(result).isEqualTo(200)
        }

        Unit
    }
}