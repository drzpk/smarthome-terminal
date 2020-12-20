package dev.drzepka.smarthome.terminal.client.data

import dev.drzepka.smarthome.terminal.common.api.element.property.PropertyModel
import dev.drzepka.smarthome.terminal.common.util.Logger

class PropertyBundle(private val bundleName: String, private val propertyValues: Map<Int, String?>) {

    private val log by Logger()

    fun setPropertyValues(properties: Collection<PropertyModel<*>>) {
        properties.forEach(this::setPropertyValue)
    }

    private fun setPropertyValue(property: PropertyModel<*>) {
        if (!propertyValues.containsKey(property.id)) {
            log.error("No property [id={}, key={}] found in the bundle {}", property.id, property.key, bundleName)
            property.value = null
            return
        }

        val rawValue = propertyValues[property.id]
        try {
            property.setValue(rawValue)
        } catch (e: Exception) {
            throw IllegalArgumentException(
                "Error while setting value for property [id=${property.id}, key=${property.key}]", e
            )
        }
    }
}