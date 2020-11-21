package dev.drzepka.smarthome.terminal.server.domain.converter

interface Converter<I, O> {

    fun convert(source: I): O?
}