package dev.drzepka.smarthome.terminal.common.transport.element

import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import dev.drzepka.smarthome.terminal.common.api.element.property.PropertyModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.IntPropertyModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.StringPropertyModel

object ElementRegistry {

    const val ELEMENT_TYPE_PROPERTY = "property"

    const val PROPERTY_TYPE_INT = "int"
    const val PROPERTY_TYPE_STRING = "string"

    val rootElementNode: ElementInnerNode = rootNode(ElementModel::elementType) {
        innerNode(ELEMENT_TYPE_PROPERTY, PropertyModel<*>::propertyType) {
            leafNode(PROPERTY_TYPE_INT, IntPropertyModel::class)
            leafNode(PROPERTY_TYPE_STRING, StringPropertyModel::class)
        }
    }
}