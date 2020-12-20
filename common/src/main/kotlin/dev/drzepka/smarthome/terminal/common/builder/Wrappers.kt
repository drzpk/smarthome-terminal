package dev.drzepka.smarthome.terminal.common.builder

import dev.drzepka.smarthome.terminal.common.api.category.CategoryModel
import dev.drzepka.smarthome.terminal.common.util.IdSpace
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ScreenCategory(private val name: String, private val description: String?) :
    ReadOnlyProperty<IdSpace, CategoryModel> {

    private var category: CategoryModel? = null

    override fun getValue(thisRef: IdSpace, property: KProperty<*>): CategoryModel {
        if (category == null)
            category = CategoryModel(thisRef.nextId(), name, description)
        return category!!
    }
}
