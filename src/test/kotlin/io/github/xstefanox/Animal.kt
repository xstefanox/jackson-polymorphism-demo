package io.github.xstefanox

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

sealed class Animal

data class Dog @JsonCreator constructor(
        @JsonProperty("color") val color: String
) : Animal()

data class Cat @JsonCreator constructor(
        @JsonProperty("age") val age: Int
) : Animal()
