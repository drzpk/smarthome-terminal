package dev.drzepka.smarthome.terminal.server.domain.value.element.property

import dev.drzepka.smarthome.terminal.server.domain.value.element.Element

/**
 * Defines property that can be changed in terminal
 */
abstract class Property<T>(val propertyType: String, val id: Int) : Element("property") {
    var title: String = ""
    var value: T? = null
    var required: Boolean = false

    var key: String? = null
        get() = field ?: title.replace(" ", "").toLowerCase()

    fun validate() {

    }
}