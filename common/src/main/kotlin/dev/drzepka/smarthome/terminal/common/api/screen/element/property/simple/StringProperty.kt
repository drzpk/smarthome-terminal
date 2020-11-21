package dev.drzepka.smarthome.terminal.common.api.screen.element.property.simple

import dev.drzepka.smarthome.terminal.common.api.screen.element.property.Property

class StringProperty(id: Int) : Property<String>("string", id) {
    var minLength: Int? = null
        set(value) {
            value?.apply { validateMinLength(this) }
            field = value
        }
    var maxLength: Int? = null
        set(value) {
            value?.apply { validateMaxLength(this) }
            field = value
        }

    private fun validateMinLength(value: Int) {

    }

    private fun validateMaxLength(value: Int) {

    }
}