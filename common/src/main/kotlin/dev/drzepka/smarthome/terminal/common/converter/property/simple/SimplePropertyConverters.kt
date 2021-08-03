package dev.drzepka.smarthome.terminal.common.converter.property.simple

import dev.drzepka.smarthome.terminal.common.converter.property.PropertyValueConverter

object IntPropertyValueConverter : PropertyValueConverter<Int> {
    override fun toString(value: Int?): String? = value?.toString()
    override fun fromString(value: String?): Int? = value?.toInt()
}

object StringPropertyValueConverter : PropertyValueConverter<String> {
    override fun toString(value: String?): String? = value
    override fun fromString(value: String?): String? = value
}