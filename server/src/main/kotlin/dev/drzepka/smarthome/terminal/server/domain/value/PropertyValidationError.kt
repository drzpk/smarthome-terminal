package dev.drzepka.smarthome.terminal.server.domain.value

import dev.drzepka.smarthome.terminal.server.domain.value.element.property.Property

class PropertyValidationError(property: Property<*>, val message: String) {
    val propertyId = property.id
    val propertyKey: String = property.key!!
}