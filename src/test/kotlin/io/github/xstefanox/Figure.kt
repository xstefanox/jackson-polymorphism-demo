package io.github.xstefanox

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

@JsonTypeInfo(use = NAME, property = "type")
@JsonSubTypes(
        Type(value = Triangle::class, name = "TRIANGLE"),
        Type(value = Rectangle::class, name = "RECTANGLE")
)
sealed class Figure

data class Triangle @JsonCreator constructor(
        @JsonProperty("size") val size: Int
) : Figure()

data class Rectangle @JsonCreator constructor(
        @JsonProperty("width") val width: Int,
        @JsonProperty("height") val height: Int
) : Figure()
