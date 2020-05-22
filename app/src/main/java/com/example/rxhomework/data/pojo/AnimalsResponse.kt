package com.example.rxhomework.data.pojo

import com.google.gson.JsonObject

data class AnimalsResponse(
    val animals: ArrayList<PetEntity>,
    val pagination: JsonObject
)