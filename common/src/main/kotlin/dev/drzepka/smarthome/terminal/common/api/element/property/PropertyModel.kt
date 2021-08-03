package dev.drzepka.smarthome.terminal.common.api.element.property

import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import dev.drzepka.smarthome.terminal.common.converter.property.PropertyValueConverter
import dev.drzepka.smarthome.terminal.common.transport.element.ElementRegistry


/**
 * Defines property that can be changed in terminal
 */
abstract class PropertyModel<T>(val propertyType: String) : ElementModel(ElementRegistry.ELEMENT_TYPE_PROPERTY) {
    abstract val valueConverter: PropertyValueConverter<T>

    var label: String = ""
    var value: T? = null
    var required: Boolean = false

    var key: String? = null
        get() = field ?: label.replace(" ", "").toLowerCase()

    fun getValue(): String? = valueConverter.toString(value)
    fun setValue(raw: String?) {
        value = valueConverter.fromString(raw)
    }

    override fun addChild(child: ElementModel) {
        throw UnsupportedOperationException("Properties can't have children")
    }
}