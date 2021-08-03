package dev.drzepka.smarthome.terminal.server.infrastructure.service

import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.domain.event.Event
import dev.drzepka.smarthome.terminal.server.domain.service.EventService
import kotlin.reflect.KClass

class EventServiceImpl : EventService {

    private val log by Logger()
    private val handlers = ArrayList<HandlerDefinition<*>>()

    override fun <E : Event> register(eventClass: KClass<E>, handler: (event: E) -> Unit) {
        val definition = HandlerDefinition(eventClass, handler)
        handlers.add(definition)
    }

    override fun publish(event: Event) {
        log.debug("Publishing event $event")

        for (handler in handlers) {
            if (handler.eventMatches(event)) {
                handler.invoke(event)
            }
        }
    }

    private class HandlerDefinition<E : Event>(
        private val eventClass: KClass<E>,
        private val handler: (event: E) -> Unit
    ) {

        fun eventMatches(event: Event): Boolean {
            return eventClass.java.isAssignableFrom(event::class.java)
        }

        @Suppress("UNCHECKED_CAST")
        fun invoke(event: Event) {
            handler.invoke(event as E)
        }
    }
}