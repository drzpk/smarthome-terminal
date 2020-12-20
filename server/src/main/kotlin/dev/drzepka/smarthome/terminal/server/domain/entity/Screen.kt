package dev.drzepka.smarthome.terminal.server.domain.entity

import dev.drzepka.smarthome.terminal.server.domain.value.element.Element

class Screen(val id: Int) {
    val elements = ArrayList<Element>()
}