package dev.drzepka.smarthome.terminal.server.application.converter.element

import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.converter.TwoWayConverter
import dev.drzepka.smarthome.terminal.server.domain.value.element.Element

abstract class AbstractElementConverter<E : Element, M : ElementModel>(private val conversionService: ConversionService) :
    TwoWayConverter<E, M> {

    fun convertElements(source: E, target: M) {
        source.children.forEach {
            val targetClass = ElementClassMapping.getOppositeClass(it::class)
            target.addChild(conversionService.convert(it, targetClass) as ElementModel)
        }
    }

    fun convertElements(source: M, target: E) {
        source.children.forEach {
            val targetClass = ElementClassMapping.getOppositeClass(it::class)
            target.children.add(conversionService.convert(it, targetClass) as Element)
        }
    }
}