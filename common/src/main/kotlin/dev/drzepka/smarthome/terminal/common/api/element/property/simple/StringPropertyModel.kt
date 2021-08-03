package dev.drzepka.smarthome.terminal.common.api.element.property.simple

import dev.drzepka.smarthome.terminal.common.api.element.property.PropertyModel
import dev.drzepka.smarthome.terminal.common.converter.property.simple.StringPropertyValueConverter
import dev.drzepka.smarthome.terminal.common.transport.element.ElementRegistry

class StringPropertyModel : PropertyModel<String>(ElementRegistry.PROPERTY_TYPE_STRING) {
    override val valueConverter = StringPropertyValueConverter

    var minLength: Int? = null
    var maxLength: Int? = null
}