package io.github.xstefanox

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode

class PolymorphicDeserializer<T>(
        clazz: Class<T>,
        private val types: Map<String, Class<out T>>
) : StdDeserializer<T>(clazz) {

    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): T {
        val tree = parser.codec.readTree<ObjectNode>(parser)
        val type = tree.remove("type") ?: throw RuntimeException()
        val clazz = types[type.asText()] ?: throw RuntimeException()
        return parser.codec.treeToValue(tree, clazz)
    }
}
