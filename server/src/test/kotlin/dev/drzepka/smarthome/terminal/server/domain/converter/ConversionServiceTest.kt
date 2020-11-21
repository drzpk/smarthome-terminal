package dev.drzepka.smarthome.terminal.server.domain.converter

import org.assertj.core.api.BDDAssertions.*
import org.junit.jupiter.api.Test

class ConversionServiceTest {

    @Test
    fun `should convert to desired type`() {
        val conversionService = ConversionService()
        conversionService.addConverter(TestConverter())

        val result = conversionService.convert<String>(123)
        then(result).isEqualTo("123")
    }

    @Test
    fun `should return null if no converter is available`() {
        val conversionService = ConversionService()
        val result = conversionService.convert<String?>(123)
        then(result).isNull()
    }

    private class TestConverter : Converter<Int, String> {
        override fun convert(source: Int): String? = source.toString()
    }
}