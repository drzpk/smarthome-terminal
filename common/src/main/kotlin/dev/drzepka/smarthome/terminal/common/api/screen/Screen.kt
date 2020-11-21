package dev.drzepka.smarthome.terminal.common.api.screen

import dev.drzepka.smarthome.terminal.common.api.screen.element.Element

class Screen(
    val id: Int,
    val customName: String? = null,
    val customDescription: String? = null
) {

    val elements = ArrayList<Element>()

    fun addElement(element: Element): Screen {
        elements.add(element)
        return this
    }
}