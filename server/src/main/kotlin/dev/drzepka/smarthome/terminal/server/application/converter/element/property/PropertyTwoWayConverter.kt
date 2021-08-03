package dev.drzepka.smarthome.terminal.server.application.converter.element.property

import dev.drzepka.smarthome.terminal.common.api.element.property.PropertyModel
import dev.drzepka.smarthome.terminal.server.domain.converter.TwoWayConverter
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.Property

abstract class PropertyTwoWayConverter<T, L : Property<T>, R : PropertyModel<T>> : TwoWayConverter<L, R> {

    fun convertPropertyR(property: Property<T>, model: PropertyModel<T>) {
        model.label = property.label
        model.key = property.key
        model.required = property.required
        model.value = property.value
    }

    fun convertPropertyL(model: PropertyModel<T>, property: Property<T>) {
        property.label = model.label
        property.key = model.key
        property.required = model.required
        property.value = model.value
    }
}