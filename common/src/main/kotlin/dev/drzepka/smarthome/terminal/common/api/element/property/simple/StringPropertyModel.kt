package dev.drzepka.smarthome.terminal.common.api.element.property.simple

import dev.drzepka.smarthome.terminal.common.api.element.property.PropertyModel
import dev.drzepka.smarthome.terminal.common.transport.element.ElementRegistry

class StringPropertyModel : PropertyModel<String>(ElementRegistry.PROPERTY_TYPE_STRING) {
    var minLength: Int? = null
    var maxLength: Int? = null

    override fun getValue(): String? {
        return value
    }

    override fun setValue(raw: String?) {
        value = raw
    }
}