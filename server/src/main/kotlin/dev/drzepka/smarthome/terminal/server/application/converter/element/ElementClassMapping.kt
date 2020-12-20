package dev.drzepka.smarthome.terminal.server.application.converter.element

import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.IntPropertyModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.StringPropertyModel
import dev.drzepka.smarthome.terminal.server.domain.value.element.Element
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple.IntProperty
import dev.drzepka.smarthome.terminal.server.domain.value.element.property.simple.StringProperty
import kotlin.reflect.KClass

object ElementClassMapping {

    private val mappings = ArrayList<Pair<KClass<out Element>, KClass<out ElementModel>>>()

    init {
        addMapping(IntProperty::class, IntPropertyModel::class)
        addMapping(StringProperty::class, StringPropertyModel::class)
    }

    fun getOppositeClass(input: KClass<*>): KClass<*> {
        for (mapping in mappings) {
            if (mapping.first == input)
                return mapping.second
            else if (mapping.second == input)
                return mapping.first
        }

        throw IllegalStateException("No mapping found for class ${input.qualifiedName}")
    }

    private fun addMapping(domainClass: KClass<out Element>, modelClass: KClass<out ElementModel>) {
        mappings.add(domainClass to modelClass)
    }
}
