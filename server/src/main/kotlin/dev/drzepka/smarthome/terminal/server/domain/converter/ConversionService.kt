package dev.drzepka.smarthome.terminal.server.domain.converter

import kotlin.reflect.KClass

class ConversionService {

    private val conversionMap = HashMap<KClass<*>, HashMap<KClass<*>, Converter<*, *>>>()

    inline fun <reified I, reified O> addConverter(converter: Converter<I, O>) {
        addConverter(converter, I::class, O::class)
    }

    fun addConverter(converter: Converter<*, *>, sourceType: KClass<*>, targetType: KClass<*>) {
        val conversionToMap = conversionMap.computeIfAbsent(sourceType) { HashMap() }
        conversionToMap.computeIfAbsent(targetType) { converter }
    }

    inline fun <reified O : Any?> convert(source: Any): O {
        return convert(source, O::class) as O
    }

    @Suppress("UNCHECKED_CAST")
    fun convert(source: Any, targetType: KClass<*>): Any? {
        val targetConverters = conversionMap[source::class] ?: return null
        val converter = targetConverters[targetType] as Converter<Any, Any>?
        return converter?.convert(source)
    }


}