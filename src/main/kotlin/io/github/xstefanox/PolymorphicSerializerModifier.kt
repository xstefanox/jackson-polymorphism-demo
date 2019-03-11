package io.github.xstefanox

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase

class PolymorphicSerializerModifier<T>(
        private val property: String,
        private val types: Map<Class<out T>, String>) : BeanSerializerModifier() {

    override fun modifySerializer(config: SerializationConfig, beanDesc: BeanDescription, serializer: JsonSerializer<*>): JsonSerializer<*> {

        if (serializer is BeanSerializerBase) {

            return PolymorphicSerializer(serializer, property, types)
        }

        return serializer
    }
}
