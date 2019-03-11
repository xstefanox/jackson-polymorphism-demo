package io.github.xstefanox

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanSerializer
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase

class PolymorphicSerializer<T>(
        src: BeanSerializerBase,
        private val property: String,
        private val types: Map<Class<out T>, String>
) : BeanSerializer(src) {

    override fun serializeFields(bean: Any, gen: JsonGenerator, provider: SerializerProvider) {
        super.serializeFields(bean, gen, provider)
        gen.writeStringField(property, types[bean::class.java])
    }
}
