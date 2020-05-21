package com.example.rxhomework.pojo

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class AnimalsResponse(
    val animals: JsonArray,
    val pagination: JsonObject
)