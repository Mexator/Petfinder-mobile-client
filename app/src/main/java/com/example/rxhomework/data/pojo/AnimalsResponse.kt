package com.example.rxhomework.data.pojo

import com.example.rxhomework.storage.PetEntity
import com.google.gson.JsonObject

data class AnimalsResponse(
    val animals: ArrayList<Pet>,
    val pagination: JsonObject
)