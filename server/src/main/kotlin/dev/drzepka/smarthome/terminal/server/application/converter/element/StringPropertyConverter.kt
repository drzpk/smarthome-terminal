package dev.drzepka.smarthome.terminal.server.application.converter.element

import dev.drzepka.smarthome.terminal.common.api.element.property.simple.StringPropertyModel
import dev.drzepka.smarthome.terminal.server.domain.converter.TwoWayConverter
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple.StringProperty

class StringPropertyConverter : TwoWayConverter<StringProperty, StringPropertyModel> {

    override fun convertR(source: StringProperty): StringPropertyModel? {
        return StringPropertyModel().apply {
            id = source.id
            key = source.key
            value = source.value
            minLength = source.minLength
            maxLength = source.maxLength
        }
    }

    override fun convertL(source: StringPropertyModel): StringProperty? {
        return StringProperty(source.id, source.minLength, source.maxLength)
    }
}