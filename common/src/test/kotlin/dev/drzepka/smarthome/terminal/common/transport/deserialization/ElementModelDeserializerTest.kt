package dev.drzepka.smarthome.terminal.common.transport.deserialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.IntPropertyModel
import dev.drzepka.smarthome.terminal.common.exception.ElementDeserializationException
import dev.drzepka.smarthome.terminal.common.transport.element.ElementRegistry
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class ElementModelDeserializerTest {

    @Test
    fun `should deserialize leaf element`() {
        val serialized = """{"id":12,"width":100,"children":[],"elementType":"property","label":"","value":"123",
            |"required":false,"key":"","propertyType":"int","minValue":10,"maxValue":null}""".trimMargin()

        val model = getObjectMapper().readValue<ElementModel>(serialized, ElementModel::class.java)
        then(model).isInstanceOf(IntPropertyModel::class.java)

        val intModel = model as IntPropertyModel
        then(intModel.value).isEqualTo(123)
        then(intModel.minValue).isEqualTo(10)
        then(intModel.maxValue).isNull()
    }

    @Test
    fun `should not deserialize element with missing fields`() {
        val serialized = """{"id":12,"width":100,"children":[],"label":"","value":"123",
            |"required":false,"key":"","propertyType":"int","minValue":10,"maxValue":null}""".trimMargin()

        val mapper = getObjectMapper()
        assertThatExceptionOfType(ElementDeserializationException::class.java).isThrownBy {
            mapper.readValue<ElementModel>(serialized, ElementModel::class.java)
        }.withMessageContaining("Property 'elementType' doesn't exist or has null value")
    }

    @Test
    fun `should not deserialize element with misspelled fields`() {
        val serialized = """{"id":12,"width":100,"children":[],"elementType":"__non_existent__","label":"",
            |"value":"123","required":false,"key":"","propertyType":"int","minValue":10,"maxValue":null}""".trimMargin()

        val mapper = getObjectMapper()
        assertThatExceptionOfType(ElementDeserializationException::class.java).isThrownBy {
            mapper.readValue<ElementModel>(serialized, ElementModel::class.java)
        }.withMessageContaining("No child element node found for property name 'elementType' and value '__non_existent__'")
    }

    private fun getObjectMapper(): ObjectMapper = ObjectMapper().apply {
        val module = SimpleModule()
        module.addDeserializer(ElementModel::class.java, ElementModelDeserializer(ElementRegistry.rootElementNode))
        registerModule(module)
    }
}