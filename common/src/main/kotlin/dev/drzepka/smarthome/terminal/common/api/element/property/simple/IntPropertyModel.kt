package dev.drzepka.smarthome.terminal.common.api.element.property.simple

import dev.drzepka.smarthome.terminal.common.api.element.property.PropertyModel
import dev.drzepka.smarthome.terminal.common.converter.property.simple.IntPropertyValueConverter
import dev.drzepka.smarthome.terminal.common.transport.element.ElementRegistry

class IntPropertyModel : PropertyModel<Int>(ElementRegistry.PROPERTY_TYPE_INT) {
    override val valueConverter = IntPropertyValueConverter

    var minValue: Int? = null
    var maxValue: Int? = null
}