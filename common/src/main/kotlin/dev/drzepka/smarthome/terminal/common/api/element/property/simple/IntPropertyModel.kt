package dev.drzepka.smarthome.terminal.common.api.element.property.simple

import dev.drzepka.smarthome.terminal.common.api.element.property.PropertyModel
import dev.drzepka.smarthome.terminal.common.transport.element.ElementRegistry

class IntPropertyModel : PropertyModel<Int>(ElementRegistry.PROPERTY_TYPE_INT) {
    var minValue: Int? = null
    var maxValue: Int? = null

    override fun getValue(): String? {
        return value?.toString()
    }

    override fun setValue(raw: String?) {
        value = raw?.toInt()
    }
}