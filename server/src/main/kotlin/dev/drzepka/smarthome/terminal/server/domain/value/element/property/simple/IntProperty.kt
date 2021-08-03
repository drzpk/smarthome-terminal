package dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple

import dev.drzepka.smarthome.terminal.common.converter.property.simple.IntPropertyValueConverter
import dev.drzepka.smarthome.terminal.server.domain.value.PropertyValidationError
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.Property

class IntProperty(
    id: Int,
    val minValue: Int? = null,
    val maxValue: Int? = null
) : Property<Int>("int", id) {
    override val valueConverter = IntPropertyValueConverter

    override fun validate(errors: MutableList<PropertyValidationError>) {
        super.validate(errors)
        if (isPresent() && minValue != null && value!! < minValue)
            errors.add(PropertyValidationError(this, "Minimum value is $minValue"))
        if (isPresent() && maxValue != null && value!! > maxValue)
            errors.add(PropertyValidationError(this, "Maximum value is $maxValue"))
    }

    override fun isPresent(): Boolean = value != null
}