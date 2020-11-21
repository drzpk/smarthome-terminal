package dev.drzepka.smarthome.terminal.common.transport.message

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import dev.drzepka.smarthome.terminal.common.transport.Side
import kotlin.reflect.KClass

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
abstract class Message<Response : Any>(
    val receiverSide: Side,
    @JsonIgnore
    val responseClass: KClass<Response>
) {
    val id = System.nanoTime()
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
abstract class MessageResponse {
    var requestMessageId: Long = 0

    constructor()
    constructor(requestMessage: Message<out Any>) {
        if (requestMessage.responseClass != this::class)
            throw IllegalStateException("Request and response classes don't match") // is there a better way to do this?
        requestMessageId = requestMessage.id
    }
}

/**
 * Required to make @JsonTypeInfo annotation on the message class work (it doesn't on *normal* list because of
 * [type erasure](https://github.com/FasterXML/jackson-databind/issues/23#issuecomment-6251193))
 */
@Suppress("unused")
class MessageList : ArrayList<Message<out MessageResponse>> {
    constructor() : super()
    constructor(collection: Collection<Message<out MessageResponse>>) : super(collection)
}

@Suppress("unused")
class MessageResponseList : ArrayList<MessageResponse> {
    constructor() : super()
    constructor(collection: Collection<MessageResponse>) : super(collection)
}