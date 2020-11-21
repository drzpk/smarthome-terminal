package dev.drzepka.smarthome.terminal.server.domain.service

import com.nhaarman.mockitokotlin2.*
import dev.drzepka.smarthome.terminal.common.transport.message.PingMessage
import dev.drzepka.smarthome.terminal.server.application.converter.CategoryModelToEntityConverter
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.infrastructure.repository.InMemoryClientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ClientServiceTest {

    @Test
    fun `should send ping to client`() = runBlockingTest {
        var pingTask: (suspend CoroutineScope.() -> Unit)? = null

        val queue = mock<TerminalQueue> { }
        val scheduler = mock<Scheduler> {
            on { registerScheduledTask(any(), any(), any()) }.doAnswer {
                @Suppress("UNCHECKED_CAST")
                pingTask = it.arguments[2] as (suspend CoroutineScope.() -> Unit)
                Unit
            }
        }

        val service = getClientService(queue, scheduler)
        val client = service.registerClient("name", "secret")
        then(pingTask).overridingErrorMessage("ping task wasn't registered").isNotNull()

        //repeat(3) { delay(100) }
        pingTask!!.invoke(this)
        verify(queue, times(1)).putMessage(eq(client), any<PingMessage>())
    }

    private fun getClientService(queue: TerminalQueue, scheduler: Scheduler): ClientService {
        val conversionService = ConversionService()
        conversionService.addConverter(CategoryModelToEntityConverter())
        return ClientService(InMemoryClientRepository(), queue, conversionService, scheduler)
    }
}