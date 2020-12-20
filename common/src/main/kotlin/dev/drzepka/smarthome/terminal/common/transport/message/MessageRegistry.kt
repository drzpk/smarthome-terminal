package dev.drzepka.smarthome.terminal.common.transport.message

import kotlin.reflect.KClass

object MessageRegistry {
    val messages = ArrayList<MessageEntry>()

    init {
        registerMessage(PingMessage::class, PingMessageResponse::class)
        registerMessage(GetCategoriesMessage::class, GetCategoriesMessageResponse::class)
        registerMessage(GetScreenMessage::class, GetScreenMessageResponse::class)
        registerMessage(ScreenUpdateMessage::class, ScreenUpdateMessageResponse::class)
    }

    private fun registerMessage(
        messageClass: KClass<out Message<*>>,
        messageResponseClass: KClass<out MessageResponse>
    ) {
        messages.add(MessageEntry(messageClass, messageResponseClass))
    }

    data class MessageEntry(
        val messageClass: KClass<out Message<*>>,
        val messageResponseClass: KClass<out MessageResponse>
    )
}