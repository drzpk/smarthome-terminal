package dev.drzepka.smarthome.terminal.client.utils

import dev.drzepka.smarthome.terminal.client.IdGenerator
import dev.drzepka.smarthome.terminal.common.api.category.CategoryModel
import dev.drzepka.smarthome.terminal.common.api.screen.element.property.Property
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ScreenCategory(name: String, description: String?) : ReadOnlyProperty<Any, CategoryModel> {
    private val category = CategoryModel(IdGenerator.nextId(), name, description)
    override fun getValue(thisRef: Any, property: KProperty<*>): CategoryModel = category
}

class ScreenProperty<P : Property<*>> : ReadOnlyProperty<Any, P> {
    override fun getValue(thisRef: Any, property: KProperty<*>): P {
        TODO()
    }
}