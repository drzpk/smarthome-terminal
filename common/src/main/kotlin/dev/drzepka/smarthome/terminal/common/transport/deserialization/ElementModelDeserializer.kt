package dev.drzepka.smarthome.terminal.common.transport.deserialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import dev.drzepka.smarthome.terminal.common.exception.ElementDeserializationException
import dev.drzepka.smarthome.terminal.common.transport.element.ElementInnerNode
import dev.drzepka.smarthome.terminal.common.transport.element.ElementLeafNode
import dev.drzepka.smarthome.terminal.common.transport.element.ElementNode

class ElementModelDeserializer(private val rootElementNode: ElementNode) :
    StdDeserializer<ElementModel>(ElementModel::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ElementModel {
        val objectNode = p.readValueAsTree<ObjectNode>()

        var currentElementNode = rootElementNode
        var nestingLevel = 0

        do {
            val propertyValue = getPropertyValue(objectNode, currentElementNode.propertyName)
            currentElementNode = getNextElementNode(currentElementNode, propertyValue)
            nestingLevel++
        } while (currentElementNode !is ElementLeafNode && nestingLevel <= MAX_NESTING_LEVEL)

        if (nestingLevel > MAX_NESTING_LEVEL)
            throw ElementDeserializationException("Max nesting level ($MAX_NESTING_LEVEL) was reached")

        val targetClass = (currentElementNode as ElementLeafNode).elementClass

        val reloadedParser = objectNode.traverse()
        reloadedParser.nextToken()
        return ctxt.readValue(reloadedParser, targetClass.java)
    }

    private fun getPropertyValue(node: ObjectNode, property: String): String {
        val value = node[property]
        if (value == null) {
            throw ElementDeserializationException("Property '$property' doesn't exist or has null value")
        } else if (value !is TextNode) {
            throw ElementDeserializationException("Property '$property' must be of String type")
        }

        return value.textValue()!!
    }

    private fun getNextElementNode(currentElementNode: ElementNode, propertyValue: String): ElementNode {
        if (currentElementNode !is ElementInnerNode)
            throw ElementDeserializationException("Inner element node expected")

        return currentElementNode.children[propertyValue]
            ?: throw ElementDeserializationException("No child element node found for property name '${currentElementNode.propertyName}' and value '$propertyValue'")
    }

    companion object {
        private const val MAX_NESTING_LEVEL = 5
    }
}
