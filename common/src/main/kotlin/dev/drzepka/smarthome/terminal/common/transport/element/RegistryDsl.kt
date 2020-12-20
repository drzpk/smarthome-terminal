package dev.drzepka.smarthome.terminal.common.transport.element

import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.superclasses

fun rootNode(property: KProperty1<ElementModel, String>, init: ElementInnerNode.() -> Unit): ElementInnerNode {
    val node = ElementInnerNode(property.name)
    init(node)
    return node
}

inline fun <reified T : ElementModel> ElementInnerNode.innerNode(
    superPropertyValue: String,
    property: KProperty1<T, String>,
    init: ElementInnerNode.() -> Unit
) {
    assertDirectSuperclassOfType(T::class, this::class)
    if (!T::class.isAbstract)
        throw IllegalArgumentException("Inner node class must be abstract")

    if (this.children.containsKey(superPropertyValue))
        throw IllegalArgumentException("Element inner node identified by proeprty ${this.propertyName} already has child with key '$superPropertyValue'")

    val node = ElementInnerNode(property.name)
    init(node)
    this.children[superPropertyValue] = node
}

inline fun <reified T : ElementModel> ElementInnerNode.leafNode(superPropertyValue: String, clazz: KClass<T>) {
    assertDirectSuperclassOfType(T::class, this::class)
    if (T::class.isAbstract)
        throw IllegalArgumentException("Leaf node class must not be abstract")

    if (this.children.containsKey(superPropertyValue))
        throw IllegalArgumentException("Element inner node identified by proeprty ${this.propertyName} already has child with key '$superPropertyValue'")

    val node = ElementLeafNode("", clazz)
    this.children[superPropertyValue] = node
}

fun assertDirectSuperclassOfType(clazz: KClass<*>, expectedSuperclass: KClass<*>) {
    if (clazz.superclasses.isEmpty() || clazz.superclasses.contains(expectedSuperclass)) {
        throw IllegalArgumentException("Class ${clazz.simpleName} was expected to be the direct superclass of ${expectedSuperclass.simpleName}")
    }
}
