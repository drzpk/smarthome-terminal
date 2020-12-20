package dev.drzepka.smarthome.terminal.common.util


interface IdSpace {
    var idSpaceState: Int

    fun nextId(): Int {
        return ++idSpaceState
    }
}