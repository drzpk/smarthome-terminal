package dev.drzepka.smarthome.terminal.server.application.converter.element.property.simple

import dev.drzepka.smarthome.terminal.common.api.element.property.simple.IntPropertyModel
import dev.drzepka.smarthome.terminal.server.application.converter.element.property.PropertyTwoWayConverter
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple.IntProperty

class IntPropertyConverter : PropertyTwoWayConverter<Int, IntProperty, IntPropertyModel>() {

    override fun convertR(source: IntProperty): IntPropertyModel? {
        return IntPropertyModel().apply {
            id = source.id
            minValue = source.minValue
            maxValue = source.maxValue
            convertPropertyR(source, this)
        }
    }

    override fun convertL(source: IntPropertyModel): IntProperty? {
        return IntProperty(source.id, source.minValue, source.maxValue).apply {
            convertPropertyL(source, this)
        }
    }

}