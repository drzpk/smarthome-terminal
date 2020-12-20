package dev.drzepka.smarthome.terminal.server.application.converter.element

import dev.drzepka.smarthome.terminal.common.api.element.property.simple.IntPropertyModel
import dev.drzepka.smarthome.terminal.server.domain.converter.TwoWayConverter
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple.IntProperty

class IntPropertyConverter : TwoWayConverter<IntProperty, IntPropertyModel> {

    override fun convertR(source: IntProperty): IntPropertyModel? {
        return IntPropertyModel().apply {
            id = source.id
            key = source.key
            value = source.value
            minValue = source.minValue
            maxValue = source.maxValue
        }
    }

    override fun convertL(source: IntPropertyModel): IntProperty? {
        return IntProperty(source.id, source.minValue, source.maxValue)
    }

}