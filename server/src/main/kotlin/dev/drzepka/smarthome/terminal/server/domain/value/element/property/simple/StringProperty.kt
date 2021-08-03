package dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple

import dev.drzepka.smarthome.terminal.common.converter.property.simple.StringPropertyValueConverter
import dev.drzepka.smarthome.terminal.server.domain.value.PropertyValidationError
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.Property

class StringProperty(
    id: Int,
    val minLength: Int? = null,
    val maxLength: Int? = null
) : Property<String>("string", id) {
    override val valueConverter = StringPropertyValueConverter

    override fun validate(errors: MutableList<PropertyValidationError>) {
        super.validate(errors)
        if (isPresent() && minLength != null && value!!.length < minLength)
            errors.add(PropertyValidationError(this, "Minimum length is $minLength."))
        if (isPresent() && maxLength != null && value!!.length > maxLength)
            errors.add(PropertyValidationError(this, "Maximum length is $maxLength."))
    }

    override fun isPresent(): Boolean = value?.isNotBlank() == true
}