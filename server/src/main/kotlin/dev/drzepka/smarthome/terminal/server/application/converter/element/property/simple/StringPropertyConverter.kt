package dev.drzepka.smarthome.terminal.server.application.converter.element.property.simple

import dev.drzepka.smarthome.terminal.common.api.element.property.simple.StringPropertyModel
import dev.drzepka.smarthome.terminal.server.application.converter.element.property.PropertyTwoWayConverter
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple.StringProperty

class StringPropertyConverter : PropertyTwoWayConverter<String, StringProperty, StringPropertyModel>() {

    override fun convertR(source: StringProperty): StringPropertyModel? {
        return StringPropertyModel().apply {
            id = source.id
            minLength = source.minLength
            maxLength = source.maxLength
            convertPropertyR(source, this)
        }
    }

    override fun convertL(source: StringPropertyModel): StringProperty? {
        return StringProperty(source.id, source.minLength, source.maxLength).apply {
            convertPropertyL(source, this)
        }
    }
}