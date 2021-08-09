package dev.drzepka.smarthome.terminal.server.domain.service.screen

import dev.drzepka.smarthome.terminal.common.api.element.ScreenModel
import dev.drzepka.smarthome.terminal.common.api.screen.ProcessScreenUpdateRequest
import dev.drzepka.smarthome.terminal.common.api.screen.response.ProcessScreenUpdateResponse
import dev.drzepka.smarthome.terminal.common.api.screen.response.ScreenUpdateErrorResponse
import dev.drzepka.smarthome.terminal.common.api.screen.response.ScreenValidationErrorResponse
import dev.drzepka.smarthome.terminal.common.transport.message.GetScreenMessage
import dev.drzepka.smarthome.terminal.common.transport.message.ScreenUpdateMessage
import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import dev.drzepka.smarthome.terminal.server.domain.entity.Screen
import dev.drzepka.smarthome.terminal.server.domain.event.ClientUnregisteredEvent
import dev.drzepka.smarthome.terminal.server.domain.repository.ScreenRepository
import dev.drzepka.smarthome.terminal.server.domain.service.EventService
import dev.drzepka.smarthome.terminal.server.domain.service.TerminalQueue
import dev.drzepka.smarthome.terminal.server.domain.service.register
import dev.drzepka.smarthome.terminal.server.domain.value.PropertyValidationError
import dev.drzepka.smarthome.terminal.server.domain.value.element.Element
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.Property

class ScreenService(
    private val terminalQueue: TerminalQueue,
    private val screenRepository: ScreenRepository,
    private val conversionService: ConversionService,
    eventService: EventService
) {

    private val log by Logger()

    init {
        eventService.register<ClientUnregisteredEvent> { screenRepository.delete(it.client.id) }
    }

    suspend fun getScreen(client: Client, categoryId: Int): ScreenModel? {
        log.info("Getting screen id={} from {}", categoryId, client)
        val message = GetScreenMessage().apply {
            this.categoryId = categoryId
        }

        val response = terminalQueue.putMessage(client, message)
        if (response.screen == null) {
            log.error("Screen id={} wasn't found in {}", categoryId, client)
            return null
        } else if (response.screen?.id != categoryId) {
            log.error("Returned screen id={} doesn't match requested screen id={}", response.screen?.id, categoryId)
            return null
        }

        val screenEntity = conversionService.convert<Screen>(response.screen!!)
        screenRepository.save(client.id, screenEntity)

        return response.screen
    }

    suspend fun processUpdate(client: Client, request: ProcessScreenUpdateRequest): ProcessScreenUpdateResponse {
        val screen = screenRepository.get(client.id, request.screenId)
            ?: throw IllegalArgumentException("Screen entity ${request.screenId} wasn't found for client ${client.id}")

        val validationErrors = setElementProperties(screen, request.properties)
        if (validationErrors.isNotEmpty()) {
            val response = ScreenValidationErrorResponse()
            response.errors = validationErrors.associateBy({ error -> error.propertyId }, { error -> error.message })
            return response
        }

        return doProcessUpdate(client, screen, request.properties)
    }

    private fun setElementProperties(element: Element, values: Map<Int, String?>): List<PropertyValidationError> {
        val validationErrors = ArrayList<PropertyValidationError>()

        val queue = ArrayDeque<Element>()
        queue.add(element)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current is Property<*>) {
                setPropertyValue(current, values[current.id]).forEach { validationErrors.add(it) }
            }

            current.children.forEach { queue.add(it) }
        }

        return validationErrors
    }

    private fun setPropertyValue(property: Property<*>, value: String?): Collection<PropertyValidationError> {
        return try {
            property.setValue(value)
            val validationErrors = ArrayList<PropertyValidationError>()
            property.validate(validationErrors)
            validationErrors
        } catch (e: Exception) {
            listOf(PropertyValidationError(property, "Unable to set property value"))
        }
    }

    private suspend fun doProcessUpdate(
        client: Client,
        screen: Screen,
        properties: Map<Int, String?>
    ): ProcessScreenUpdateResponse {

        val message = ScreenUpdateMessage()
        message.screenId = screen.id
        message.propertyValues = properties

        return try {
            val messageResponse = terminalQueue.putMessage(client, message)
            conversionService.convert(messageResponse)
        } catch (e: Exception) {
            log.error("Error while processing screen update message for {}", client, e)
            val response = ScreenUpdateErrorResponse()
            response.message = "Error while processing screen update"
            response
        }
    }

}