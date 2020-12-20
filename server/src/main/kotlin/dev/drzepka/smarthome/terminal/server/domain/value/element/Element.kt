package dev.drzepka.smarthome.terminal.server.domain.value.element

/**
 * Defines a screen element
 */
abstract class Element(val elementType: String) {
    var width: Int = 100
        set(value) {
            if (value < 1 || value > 100)
                throw IllegalStateException("Element width must be between 1 and 100")
            field = value
        }

    private val children = ArrayList<Element>()

    fun addChild(child: Element) {
        children.add(child)
    }
}