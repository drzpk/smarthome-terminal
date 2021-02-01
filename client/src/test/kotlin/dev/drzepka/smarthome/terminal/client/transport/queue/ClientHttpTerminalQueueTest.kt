package dev.drzepka.smarthome.terminal.client.transport.queue

import com.fasterxml.jackson.databind.jsontype.NamedType
import dev.drzepka.smarthome.terminal.common.configurer.JacksonConfigurer
import dev.drzepka.smarthome.terminal.common.transport.Side
import dev.drzepka.smarthome.terminal.common.transport.message.Message
import dev.drzepka.smarthome.terminal.common.transport.message.MessageResponse
import dev.drzepka.smarthome.terminal.common.transport.message.PingClientMessage
import dev.drzepka.smarthome.terminal.common.transport.message.PingClientMessageResponse
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.BDDAssertions.assertThatExceptionOfType
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class ClientHttpTerminalQueueTest {

    @Test
    fun `should poll every given interval`() = runBlocking {
        val counter = AtomicInteger()
        val httpClient = createHttpClient { request ->
            counter.addAndGet(1)
            if (request.url.fullPath == "/api/terminal/queue/poll")
                respond(
                    """[{"@type":"PingClientMessage","id":23469963625500,"receiverSide":"CLIENT"}]""",
                    headers = headersOf("Content-Type", "application/json")
                )
            else
                null
        }

        val queue = ClientHttpTerminalQueue(httpClient, "/api", this, null, 50)
        queue.start()

        // Allow to switch thread to coroutine inside the queue
        repeat(3) {
            delay(60)
        }
        then(counter.get()).isGreaterThanOrEqualTo(2)

        queue.stop()
        Unit
    }

    @Test
    fun `should NOT dispatch message to client`() = runBlocking {
        val httpClient = createHttpClient { error("") }
        val queue = ClientHttpTerminalQueue(httpClient, "/api", this, null, 50)

        assertThatExceptionOfType(UnsupportedOperationException::class.java)
            .isThrownBy {
                runBlocking {
                    queue.putMessage(TestMessage(Side.CLIENT))
                }
            }

        Unit
    }

    @Test
    fun `should send message to server`() = runBlocking {
        var requestSent = false
        var handlerInvoked = false

        val httpClient = createHttpClient { request ->
            requestSent = request.method == HttpMethod.Put
            respond(
                """{"@type":"TestMessageResponse","requestMessageId":23469963625500}""",
                headers = headersOf("Content-Type", "application/json")
            )
        }

        val queue = ClientHttpTerminalQueue(httpClient, "/api", this, null, 50)
        queue.handler = object : TerminalQueue.Handler {
            @Suppress("UNCHECKED_CAST")
            override fun <Response : MessageResponse> processMessage(message: Message<Response>): Response {
                handlerInvoked = true
                return TestMessageResponse(message as TestMessage) as Response
            }
        }

        // Queue doesn't need to be started - polling isn't tested here
        queue.putMessage(TestMessage(Side.SERVER))

        then(requestSent).isTrue()
        then(handlerInvoked).isFalse()

        Unit
    }

    @Test
    fun `should handle message response from server`() = runBlocking {
        var requestReceived = false
        var requestSent = false
        var handlerInvoked = false

        val httpClient = createHttpClient { request ->
            if (request.url.fullPath == "/api/terminal/queue/poll" && request.method == HttpMethod.Get) {
                requestReceived = true
                respond(
                    """[{"@type":"PingClientMessage","id":23469963625500,"receiverSide":"CLIENT"}]""",
                    headers = headersOf("Content-Type", "application/json")
                )
            } else if (request.url.fullPath == "/api/terminal/queue/poll" && request.method == HttpMethod.Put) {
                requestSent = true
                respond("", status = HttpStatusCode.Created)
            } else {
                null
            }
        }

        val queue = ClientHttpTerminalQueue(httpClient, "/api", this, null, 50)
        queue.handler = object : TerminalQueue.Handler {
            @Suppress("UNCHECKED_CAST")
            override fun <Response : MessageResponse> processMessage(message: Message<Response>): Response {
                handlerInvoked = true
                return PingClientMessageResponse(message as PingClientMessage) as Response
            }
        }

        queue.start()
        delay(50)

        then(requestReceived).isTrue()
        then(requestSent).isTrue()
        then(handlerInvoked).isTrue()

        queue.stop()
        Unit
    }

    private fun createHttpClient(handler: MockRequestHandleScope.(request: HttpRequestData) -> HttpResponseData?): HttpClient =
        HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    val result = handler.invoke(this, request)
                    if (result != null)
                        return@addHandler result
                    else
                        error("unhandled path: ${request.method} ${request.url.fullPath}")
                }
            }

            install(JsonFeature) {
                serializer = JacksonSerializer {
                    JacksonConfigurer.configure(this)
                    registerSubtypes(NamedType(TestMessage::class.java, "TestMessage"))
                    registerSubtypes(NamedType(TestMessageResponse::class.java, "TestMessageResponse"))
                }
            }
            install(HttpTimeout)
        }

    class TestMessage(receiverSide: Side) : Message<TestMessageResponse>(receiverSide, TestMessageResponse::class)

    class TestMessageResponse : MessageResponse {
        @Suppress("unused")
        constructor() : super()
        constructor(requestMessage: TestMessage) : super(requestMessage)

    }
}