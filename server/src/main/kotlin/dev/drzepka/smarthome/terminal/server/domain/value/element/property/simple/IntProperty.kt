package dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple

import dev.drzepka.smarthome.terminal.server.domain.value.element.property.Property

class IntProperty(
    id: Int,
    val minValue: Int? = null,
    val maxValue: Int? = null
) : Property<Int>("int", id)