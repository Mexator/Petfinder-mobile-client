package com.example.rxhomework.pojo

import com.example.rxhomework.storage.PetEntity
import com.google.gson.JsonObject

data class AnimalsResponse(
    val animals: ArrayList<PetEntity>,
    val pagination: JsonObject
)