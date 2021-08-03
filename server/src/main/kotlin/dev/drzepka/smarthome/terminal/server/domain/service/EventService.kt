package dev.drzepka.smarthome.terminal.server.domain.service

import dev.drzepka.smarthome.terminal.server.domain.event.Event
import kotlin.reflect.KClass

interface EventService {
    fun <E : Event> register(eventClass: KClass<E>, handler: (event: E) -> Unit)
    fun publish(event: Event)
}

inline fun <reified E : Event> EventService.register(noinline handler: (event: E) -> Unit) {
    register(E::class, handler)
}