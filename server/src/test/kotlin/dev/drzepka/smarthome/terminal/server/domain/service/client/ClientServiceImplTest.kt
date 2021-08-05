package dev.drzepka.smarthome.terminal.server.domain.service.client

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import dev.drzepka.smarthome.terminal.server.application.converter.CategoryModelToEntityConverter
import dev.drzepka.smarthome.terminal.server.domain.Configuration
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import dev.drzepka.smarthome.terminal.server.domain.event.ClientUnregisteredEvent
import dev.drzepka.smarthome.terminal.server.domain.repository.ClientRepository
import dev.drzepka.smarthome.terminal.server.domain.service.EventService
import dev.drzepka.smarthome.terminal.server.domain.service.Scheduler
import dev.drzepka.smarthome.terminal.server.domain.service.TerminalQueue
import dev.drzepka.smarthome.terminal.server.infrastructure.repository.InMemoryClientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import java.time.Duration
import java.time.Instant

@ExperimentalCoroutinesApi
class ClientServiceImplTest {

    private lateinit var clientRepository: ClientRepository
    private lateinit var eventService: EventService

    @BeforeEach
    fun prepare() {
        clientRepository = InMemoryClientRepository()
        eventService = mock { }
    }

    @Test
    fun `should register scheduled task`() {
        val queue = mock<TerminalQueue> {}
        val scheduler = mock<Scheduler> {}

        getService(queue, scheduler)

        verify(scheduler, times(1)).registerScheduledTask(anyString(), any(), any())
    }

    @Test
    fun `should find client`() = runBlockingTest {
        val client = Client(11, "name")
        clientRepository.save(client)

        val queue = mock<TerminalQueue> {}
        val scheduler = mock<Scheduler> {}

        val found = getService(queue, scheduler).findClient("name", false)
        then(found).isSameAs(client)
    }

    @Test
    fun `should unregister inactive clients`() = runBlockingTest {
        val client = Client(1, "name")
        client.lastPingTime = Instant.now().minus(Configuration.maxClientInactivity).minusSeconds(1)
        clientRepository.save(client)

        val queue = mock<TerminalQueue> {}
        val scheduler = object : Scheduler {
            lateinit var handler: suspend CoroutineScope.() -> Unit
            override fun registerScheduledTask(
                taskName: String,
                period: Duration,
                handler: suspend CoroutineScope.() -> Unit
            ) {
                this.handler = handler
            }
        }

        getService(queue, scheduler)
        scheduler.handler.invoke(this)

        then(clientRepository.findAll().isEmpty()).isTrue
        verify(eventService, times(1)).publish(any<ClientUnregisteredEvent>())
    }

    @Test
    fun `should re-register client if it's already registered`() {
        val queue = mock<TerminalQueue> {}
        val scheduler = mock<Scheduler> {}
        val service = getService(queue, scheduler)

        service.registerClient("clientName")
        val registered1 = clientRepository.findByName("clientName")
        then(registered1).isNotNull
        verify(eventService, times(0)).publish(any<ClientUnregisteredEvent>())

        service.registerClient("clientName")
        val registered2 = clientRepository.findByName("clientName")
        then(registered2).isNotNull
        then(registered2!!.id).isNotEqualTo(registered1!!.id)
        verify(eventService, times(1)).publish(any<ClientUnregisteredEvent>())
    }

    private fun getService(queue: TerminalQueue, scheduler: Scheduler): ClientServiceImpl {
        val conversionService = ConversionService()
        conversionService.addConverter(CategoryModelToEntityConverter())
        return ClientServiceImpl(
            clientRepository,
            queue,
            conversionService,
            eventService,
            scheduler
        )
    }
}