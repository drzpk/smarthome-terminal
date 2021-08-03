package dev.drzepka.smarthome.terminal.server.domain.value.element.property

import dev.drzepka.smarthome.terminal.common.converter.property.PropertyValueConverter
import dev.drzepka.smarthome.terminal.server.domain.value.PropertyValidationError
import dev.drzepka.smarthome.terminal.server.domain.value.element.Element

/**
 * Defines property that can be changed in terminal
 */
abstract class Property<T>(val propertyType: String, val id: Int) : Element("property") {
    protected abstract val valueConverter: PropertyValueConverter<T>

    var label: String = ""
    var value: T? = null
    var required: Boolean = false

    var key: String? = null
        get() = field ?: label.replace(" ", "").toLowerCase()

    open fun validate(errors: MutableList<PropertyValidationError>) {
        if(!isPresent()) {
            errors.add(PropertyValidationError(this, "Value is required."))
        }
    }

    fun setValue(value: String?) {
        this.value = valueConverter.fromString(value)
    }

    /**
     * Returns whether this property is present. Used for validating requirements ([required]).
     */
    abstract fun isPresent(): Boolean
}