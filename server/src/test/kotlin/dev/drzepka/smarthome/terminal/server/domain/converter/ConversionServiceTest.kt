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
    fun `should convert using two-way converter`() {
        class TestTwoWayConverter : TwoWayConverter<Int, Double> {
            override fun convertR(source: Int): Double? = source.toDouble()
            override fun convertL(source: Double): Int? = source.toInt()
        }

        val conversionService = ConversionService()
        conversionService.addConverter(TestTwoWayConverter())

        val result1 = conversionService.convert(1, Double::class)
        then(result1).isEqualTo(1.0)
        val result2 = conversionService.convert(2.0, Int::class)
        then(result2).isEqualTo(2)
    }

    @Test
    fun `should return null if no converter is available`() {
        val conversionService = ConversionService()
        val result = conversionService.convert<String?>(123)
        then(result).isNull()
    }

    @Test
    fun `should throw exception if converter with the same generic types already exists`() {
        val conversionService = ConversionService()

        assertThatCode {
            conversionService.addConverter(TestConverter())
        }.doesNotThrowAnyException()

        assertThatIllegalArgumentException().isThrownBy {
            conversionService.addConverter(TestConverter())
        }
    }

    private class TestConverter : Converter<Int, String> {
        override fun convert(source: Int): String? = source.toString()
    }
}