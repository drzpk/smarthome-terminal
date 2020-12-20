package dev.drzepka.smarthome.terminal.common.transport.element

import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import dev.drzepka.smarthome.terminal.common.api.element.property.PropertyModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.IntPropertyModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.StringPropertyModel
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class RegistryDslTest {

    @Test
    fun `should build correct registry`() {
        val root = rootNode(ElementModel::elementType) {
            innerNode(ElementRegistry.ELEMENT_TYPE_PROPERTY, PropertyModel<*>::propertyType) {
                leafNode(ElementRegistry.PROPERTY_TYPE_INT, IntPropertyModel::class)
                leafNode(ElementRegistry.PROPERTY_TYPE_STRING, StringPropertyModel::class)
            }
        }

        then(root.propertyName).isEqualTo("elementType")
        then(root.children).hasSize(1)
        then(root.children).containsKey("property")

        val propertyModelChild = root.children["property"]
        then(propertyModelChild).isInstanceOf(ElementInnerNode::class.java)
        then(propertyModelChild!!.propertyName).isEqualTo("propertyType")
        then((propertyModelChild as ElementInnerNode).children).hasSize(2)
        then(propertyModelChild.children).containsOnlyKeys("int", "string")

        val intLeafNode = propertyModelChild.children["int"]
        then(intLeafNode).isInstanceOf(ElementLeafNode::class.java)
        then((intLeafNode as ElementLeafNode).elementClass).isEqualTo(IntPropertyModel::class)

        val stringLeafNode = propertyModelChild.children["string"]
        then(stringLeafNode).isInstanceOf(ElementLeafNode::class.java)
        then((stringLeafNode as ElementLeafNode).elementClass).isEqualTo(StringPropertyModel::class)
    }
}