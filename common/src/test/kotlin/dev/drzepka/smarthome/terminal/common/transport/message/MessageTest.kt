package dev.drzepka.smarthome.terminal.common.transport.message

import com.fasterxml.jackson.databind.ObjectMapper
import dev.drzepka.smarthome.terminal.common.configurer.JacksonConfigurer
import dev.drzepka.smarthome.terminal.common.transport.Side
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import java.util.regex.Pattern

class MessageTest {

    private val serializedPingMessage =
        Pattern.compile("""\[\{"@type":"PingMessage","id":\d+,"receiverSide":"CLIENT"}]""")
    private val serializedPingMessageResponse =
        Pattern.compile("""\[\{"@type":"PingMessageResponse","requestMessageId":\d+}]""")


    @Test
    fun `should serialize message`() {
        val message = PingMessage()
        val objectMapper = getObjectMapper()

        val serialized = objectMapper.writeValueAsString(MessageList(listOf(message)))
        then(serialized).matches(serializedPingMessage)
    }

    @Test
    fun `should serialize message response`() {
        val response = PingMessageResponse(PingMessage())
        val objectMapper = getObjectMapper()

        val serialized = objectMapper.writeValueAsString(MessageResponseList(listOf(response)))
        then(serialized).matches(serializedPingMessageResponse)
    }

    @Test
    fun `should deserialize message`() {
        val serialized = """[{"@type":"PingMessage","id":23469963625500,"receiverSide":"CLIENT"}]"""
        val objectMapper = getObjectMapper()

        val deserialized = objectMapper.readValue(serialized, MessageList::class.java)
        val item = deserialized.first()

        then(item).isInstanceOf(PingMessage::class.java)
        then(item.id).isEqualTo(23469963625500L)
        then(item.receiverSide).isEqualTo(Side.CLIENT)
    }

    @Test
    fun `should deserialize message response`() {
        val serialized = """{"@type":"PingMessageResponse","requestMessageId":23469963625500}"""
        val objectMapper = getObjectMapper()

        val deserialized = objectMapper.readValue(serialized, PingMessageResponse::class.java)

        then(deserialized).isInstanceOf(PingMessageResponse::class.java)
        then(deserialized.requestMessageId).isEqualTo(23469963625500L)
    }

    private fun getObjectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        JacksonConfigurer.configure(mapper)
        return mapper
    }
}