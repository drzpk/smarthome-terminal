package dev.drzepka.smarthome.terminal.common.converter.property

interface PropertyValueConverter<T> {
    fun toString(value: T?): String?
    fun fromString(value: String?): T?
}
