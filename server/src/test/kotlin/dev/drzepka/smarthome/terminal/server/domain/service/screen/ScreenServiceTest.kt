package dev.drzepka.smarthome.terminal.server.domain.service.screen

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import dev.drzepka.smarthome.terminal.common.api.element.ScreenModel
import dev.drzepka.smarthome.terminal.common.api.screen.ProcessScreenUpdateRequest
import dev.drzepka.smarthome.terminal.common.api.screen.response.ScreenUpdatedResponse
import dev.drzepka.smarthome.terminal.common.api.screen.response.ScreenValidationErrorResponse
import dev.drzepka.smarthome.terminal.common.transport.message.GetScreenMessage
import dev.drzepka.smarthome.terminal.common.transport.message.GetScreenMessageResponse
import dev.drzepka.smarthome.terminal.common.transport.message.ScreenUpdateMessage
import dev.drzepka.smarthome.terminal.common.transport.message.ScreenUpdateMessageResponse
import dev.drzepka.smarthome.terminal.server.application.converter.element.ScreenConverter
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import dev.drzepka.smarthome.terminal.server.domain.entity.Screen
import dev.drzepka.smarthome.terminal.server.domain.repository.ScreenRepository
import dev.drzepka.smarthome.terminal.server.domain.service.EventService
import dev.drzepka.smarthome.terminal.server.domain.service.TerminalQueue
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple.StringProperty
import dev.drzepka.smarthome.terminal.server.infrastructure.repository.InMemoryScreenRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class ScreenServiceTest {

    private lateinit var terminalQueue: TerminalQueue
    private lateinit var screenRepository: ScreenRepository
    private lateinit var conversionService: ConversionService
    private lateinit var eventService: EventService

    @BeforeEach
    fun prepare() {
        terminalQueue = mock {}
        screenRepository = InMemoryScreenRepository()
        conversionService = ConversionService().apply { addConverter(ScreenConverter(this)) }
        eventService = mock {}
    }

    @Test
    fun `should return screen`() = runBlockingTest {
        val response = GetScreenMessageResponse()
        response.screen = ScreenModel().apply {
            id = 2
        }

        `when`(terminalQueue.putMessage(any(), any<GetScreenMessage>())).thenReturn(response)

        getService().getScreen(Client(1, "name"), 2)
        then(screenRepository.get(1, 2)).isNotNull()
    }

    @Test
    fun `should process screen update`() = runBlockingTest {
        val client = Client(10, "name")
        val screen = Screen(20)
        val property = StringProperty(30)
        screen.addChild(property)
        screenRepository.save(client.id, screen)

        val request = ProcessScreenUpdateRequest()
        request.screenId = screen.id
        request.properties = mapOf(
            Pair(30, "string value")
        )

        val clientResponse = ScreenUpdateMessageResponse()
        clientResponse.status = ScreenUpdateMessageResponse.Status.UPDATED
        `when`(terminalQueue.putMessage(any(), any<ScreenUpdateMessage>())).thenReturn(clientResponse)

        val response = getService().processUpdate(client, request)

        then(response).isInstanceOf(ScreenUpdatedResponse::class.java)
        then(property.value).isEqualTo("string value")
    }

    @Test
    fun `should handle validation error during screen update`() = runBlockingTest {
        val client = Client(10, "name")
        val screen = Screen(20)
        val property = StringProperty(30, minLength = 10)
        screen.addChild(property)
        screenRepository.save(client.id, screen)

        val request = ProcessScreenUpdateRequest()
        request.screenId = screen.id
        request.properties = mapOf(
            Pair(30, "short")
        )

        val response = getService().processUpdate(client, request)
        then(response).isInstanceOf(ScreenValidationErrorResponse::class.java)

        val errorResponse = response as ScreenValidationErrorResponse
        then(errorResponse.errors).hasSize(1)
        then(errorResponse.errors?.get(30)).isEqualTo("Minimum length is 10.")
    }

    private fun getService(): ScreenService {
        return ScreenService(terminalQueue, screenRepository, conversionService, eventService)
    }
}