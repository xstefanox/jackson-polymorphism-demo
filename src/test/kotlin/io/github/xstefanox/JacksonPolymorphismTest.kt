package io.github.xstefanox

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Plain [ObjectMapper] that determines which subtype use for serialization and deserialization by reading the parent
 * class annotations.
 */
private val OBJECT_MAPPER = ObjectMapper()

/**
 * Custom [ObjectMapper] with subtypes configured with a dedicated module.
 */
private val CUSTOM_OBJECT_MAPPER = ObjectMapper()
        .registerModule(polymorphicTypes("type", mapOf(
                "DOG" to Dog::class.java,
                "CAT" to Cat::class.java
        )))

class JacksonPolymorphismTest {

    @Test
    fun `object annotation serialization`() {

        val triangle = OBJECT_MAPPER.writeValueAsString(Triangle(10))
        val rectangle = OBJECT_MAPPER.writeValueAsString(Rectangle(12, 15))

        triangle shouldMatchJson """
            {
                "type": "TRIANGLE",
                "size": 10
            }
            """

        rectangle shouldMatchJson """
            {
                "type": "RECTANGLE",
                "width": 12,
                "height": 15
            }
            """
    }

    @Test
    fun `object annotation deserialization`() {

        val triangle = OBJECT_MAPPER.readValue("""{"type":"TRIANGLE","size":10}""", Figure::class.java)
        val rectangle = OBJECT_MAPPER.readValue("""{"type":"RECTANGLE","width":12,"height":15}""", Figure::class.java)

        triangle shouldBe Triangle(10)
        rectangle shouldBe Rectangle(12, 15)
    }

    @Test
    fun `collection annotation serialization`() {

        val triangle = OBJECT_MAPPER.writeValueAsString(listOf(Triangle(10)))
        val rectangle = OBJECT_MAPPER.writeValueAsString(listOf(Rectangle(12, 15)))

        triangle shouldMatchJson """
            [
                {
                    "type": "TRIANGLE",
                    "size": 10
                }
            ]
            """

        rectangle shouldMatchJson """
            [
                {
                    "type": "RECTANGLE",
                    "width": 12,
                    "height": 15
                }
            ]
            """
    }

    @Test
    fun `collection annotation deserialization`() {

        val triangle = OBJECT_MAPPER.readValue<List<Figure>>("""[{"type":"TRIANGLE","size":10}]""", object : TypeReference<List<Figure>>() {})
        val rectangle = OBJECT_MAPPER.readValue<List<Figure>>("""[{"type":"RECTANGLE","width":12,"height":15}]""", object : TypeReference<List<Figure>>() {})

        triangle shouldBe listOf(Triangle(10))
        rectangle shouldBe listOf(Rectangle(12, 15))
    }

    @Test
    fun `object custom serializer serialization`() {

        val dog = CUSTOM_OBJECT_MAPPER.writeValueAsString(Dog("brown"))
        val cat = CUSTOM_OBJECT_MAPPER.writeValueAsString(Cat(12))

        dog shouldMatchJson """
            {
                "type": "DOG",
                "color": "brown"
            }
            """

        cat shouldMatchJson """
            {
                "type": "CAT",
                "age": 12
            }
            """
    }

    @Test
    fun `object custom serializer deserialization`() {

        val dog = CUSTOM_OBJECT_MAPPER.readValue("""{"type":"DOG","color":"brown"}""", Animal::class.java)
        val cat = CUSTOM_OBJECT_MAPPER.readValue("""{"type":"CAT","age":12}""", Animal::class.java)

        dog shouldBe Dog("brown")
        cat shouldBe Cat(12)
    }

    @Test
    fun `object collection custom serializer serialization`() {

        val dog = CUSTOM_OBJECT_MAPPER.writeValueAsString(listOf(Dog("brown")))
        val cat = CUSTOM_OBJECT_MAPPER.writeValueAsString(listOf(Cat(12)))

        dog shouldMatchJson """
            [
                {
                    "type": "DOG",
                    "color": "brown"
                }
            ]
            """

        cat shouldMatchJson """
            [
                {
                "type": "CAT",
                "age": 12
                }
            ]
            """
    }
}

/**
 * Compare 2 [String]s by interpreting them as JSON objects.
 */
infix fun String.shouldMatchJson(json: String) {

    val objectMapper = ObjectMapper()

    assertEquals(objectMapper.readTree(json), objectMapper.readTree(this))
}
