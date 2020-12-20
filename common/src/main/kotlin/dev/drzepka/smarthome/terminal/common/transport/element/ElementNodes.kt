package dev.drzepka.smarthome.terminal.common.transport.element

import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import kotlin.reflect.KClass

sealed class ElementNode(val propertyName: String)

class ElementInnerNode(propertyName: String) : ElementNode(propertyName) {
    val children = HashMap<String, ElementNode>()
}

class ElementLeafNode(propertyName: String, val elementClass: KClass<out ElementModel>) : ElementNode(propertyName)
