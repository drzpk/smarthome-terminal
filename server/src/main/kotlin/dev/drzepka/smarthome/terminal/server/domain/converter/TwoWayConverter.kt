package dev.drzepka.smarthome.terminal.server.domain.converter

interface TwoWayConverter<L, R> {
    fun convertR(source: L): R?
    fun convertL(source: R): L?
}