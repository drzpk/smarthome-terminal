package dev.drzepka.smarthome.terminal.common.api.screen.element.property

import dev.drzepka.smarthome.terminal.common.api.screen.element.Element


/**
 * Defines property that can be changed in terminal
 */
abstract class Property<T>(val propertyType: String, val id: Int) : Element("property") {
    var title: String = ""
    var value: T? = null
    var required: Boolean = false

    var key: String? = null
        get() = field ?: title.replace(" ", "").toLowerCase()
}