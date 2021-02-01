package dev.drzepka.smarthome.terminal.server.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dev.drzepka.smarthome.terminal.common.api.clients.RegisterClientResponse
import dev.drzepka.smarthome.terminal.server.terminalServer
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class ClientRegistrationTest {

    @Test
    fun `should register and unregister client`() = withTestApplication(Application::terminalServer) {
        var apiKey: String
        handleRequest(HttpMethod.Post, "/api/clients/register") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{ "name": "test", "password": "test-client-password-123" }""")
        }.apply {
            val result = response.status()!!.value
            then(result).isEqualTo(200)

            val mapper = ObjectMapper()
            mapper.registerModule(KotlinModule())
            apiKey =
                mapper.readValue<RegisterClientResponse>(response.content, RegisterClientResponse::class.java).apiKey
        }

        handleRequest(HttpMethod.Post, "/api/clients/unregister") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer $apiKey")
        }.apply {
            val result = response.status()!!.value
            then(result).isEqualTo(204)
        }

        Unit
    }
}