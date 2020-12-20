package dev.drzepka.smarthome.terminal.common.configurer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dev.drzepka.smarthome.terminal.common.api.element.ElementModel
import dev.drzepka.smarthome.terminal.common.transport.deserialization.ElementModelDeserializer
import dev.drzepka.smarthome.terminal.common.transport.element.ElementRegistry
import dev.drzepka.smarthome.terminal.common.transport.message.MessageRegistry
import kotlin.reflect.KClass

object JacksonConfigurer {

    fun configure(mapper: ObjectMapper) {
        mapper.registerKotlinModule()
        registerCustomDeserializers(mapper)
        registerMessageClasses(mapper)
    }

    private fun registerCustomDeserializers(mapper: ObjectMapper) {
        val module = SimpleModule()
        module.addDeserializer(ElementModel::class.java, ElementModelDeserializer(ElementRegistry.rootElementNode))
        mapper.registerModule(module)
    }

    private fun registerMessageClasses(mapper: ObjectMapper) {
        MessageRegistry.messages.forEach {
            checkForNoArgsConstructor(it.messageClass)
            checkForNoArgsConstructor(it.messageResponseClass)

            mapper.registerSubtypes(NamedType(it.messageClass.java, it.messageClass.simpleName))
            mapper.registerSubtypes(NamedType(it.messageResponseClass.java, it.messageResponseClass.simpleName))
        }
    }

    private fun checkForNoArgsConstructor(clazz: KClass<out Any>) {
        try {
            clazz.java.getDeclaredConstructor()
        } catch (e: NoSuchMethodException) {
            throw IllegalArgumentException("Message class ${clazz.simpleName} must have a no-arg constructor")
        }
    }

}