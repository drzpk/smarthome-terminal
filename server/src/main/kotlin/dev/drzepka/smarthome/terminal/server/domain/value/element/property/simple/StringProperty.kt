package dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple

import dev.drzepka.smarthome.terminal.server.domain.value.element.property.Property

class StringProperty(
    id: Int,
    val minLength: Int? = null,
    val maxLength: Int? = null
) : Property<String>("string", id) {

}