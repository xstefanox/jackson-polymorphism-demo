@file:JvmName("Utils")

package io.github.xstefanox

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleModule

inline fun <reified T> polymorphicTypes(property: String, types: Map<String, Class<out T>>): SimpleModule {

    return object : SimpleModule() {

        init {
            addDeserializer(T::class.java, PolymorphicDeserializer(T::class.java, types))
        }

        override fun setupModule(context: Module.SetupContext) {

            super.setupModule(context)

            val reversedTypes = types.entries.associate { (k, v) -> v to k }

            context.addBeanSerializerModifier(PolymorphicSerializerModifier(property, reversedTypes))
        }

    }
}
