package dev.drzepka.smarthome.terminal.server.infrastructure.service

import dev.drzepka.smarthome.terminal.server.domain.event.Event
import dev.drzepka.smarthome.terminal.server.domain.service.EventService
import dev.drzepka.smarthome.terminal.server.domain.service.register
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class EventServiceImplTest {

    @Test
    fun `should handle events - direct class`() {
        val service = getService()

        var handlerParam: Any? = null
        service.register<DirectClassEvent> {
            handlerParam = it
        }

        service.publish(UnusedEvent())
        then(handlerParam).isNull()

        val event = DirectClassEvent()
        service.publish(event)
        then(handlerParam).isSameAs(event)
    }

    @Test
    fun `should hanlde events - polymorphism`() {
        val service = getService()

        var handlerParam: Any? = null
        service.register<PolymorphicParentEvent> {
            handlerParam = it
        }

        val event = PolymorphicChildEvent()
        service.publish(event)
        then(handlerParam).isSameAs(event)
    }

    private fun getService(): EventService {
        return EventServiceImpl()
    }

    private class UnusedEvent: Event

    private class DirectClassEvent : Event

    private open class PolymorphicParentEvent : Event

    private class PolymorphicChildEvent : PolymorphicParentEvent()
}