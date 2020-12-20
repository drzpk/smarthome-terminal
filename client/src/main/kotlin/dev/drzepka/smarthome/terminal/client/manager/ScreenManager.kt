package dev.drzepka.smarthome.terminal.client.manager

import dev.drzepka.smarthome.terminal.client.data.PropertyBundle
import dev.drzepka.smarthome.terminal.client.data.UpdateResult
import dev.drzepka.smarthome.terminal.client.data.UpdateStatus
import dev.drzepka.smarthome.terminal.common.api.category.CategoryModel
import dev.drzepka.smarthome.terminal.common.api.element.ScreenModel
import dev.drzepka.smarthome.terminal.common.api.element.property.PropertyModel
import dev.drzepka.smarthome.terminal.common.transport.message.ScreenUpdateMessage
import dev.drzepka.smarthome.terminal.common.transport.message.ScreenUpdateMessageResponse
import dev.drzepka.smarthome.terminal.common.util.IdSpace

abstract class ScreenManager(name: String, description: String?) : IdSpace {
    override var idSpaceState = 0
    var id = 0
        set(value) {
            field = value
            category.id = value
        }
    val category = CategoryModel(0, name, description)

    fun processUpdate(message: ScreenUpdateMessage): ScreenUpdateMessageResponse {
        val bundle = PropertyBundle("ScreenManager__${category.name}", message.propertyValues)
        bundle.setPropertyValues(getTrackedProperties())

        val result = updateScreen()
        val response = ScreenUpdateMessageResponse(message)
        response.status = ScreenUpdateMessageResponse.Status.valueOf(result.status.name)
        response.message = result.message
        return response
    }

    /**
     * Called when this screen is requested.
     */
    abstract fun getScreen(): ScreenModel

    /**
     * Returns a collection of properties that will be populated with current values
     * before calling [updateScreen].
     */
    protected open fun getTrackedProperties(): Collection<PropertyModel<*>> = emptyList()

    /**
     * Called right after [getTrackedProperties] to process screen update
     */
    protected open fun updateScreen(): UpdateResult = UpdateResult(UpdateStatus.UNKNOWN, null)
}
