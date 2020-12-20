package dev.drzepka.smarthome.terminal.client.data

import dev.drzepka.smarthome.terminal.common.api.element.property.simple.IntPropertyModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.StringPropertyModel
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class PropertyBundelTest {

    @Test
    fun `should set values`() {
        val values = HashMap<Int, String>()
        values[1] = "test string"
        values[2] = "1234"

        val stringProperty = StringPropertyModel().apply {
            id = 1
        }
        val intProperty = IntPropertyModel().apply {
            id = 2
        }

        val bundle = PropertyBundle("test bundle", values)
        bundle.setPropertyValues(listOf(stringProperty, intProperty))

        then(stringProperty.value).isEqualTo("test string")
        then(intProperty.value).isEqualTo(1234)
    }
}