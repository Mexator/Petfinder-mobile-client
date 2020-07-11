package com.mexator.petfinder_client.data.pojo

data class AnimalsResponse(
    val animals: ArrayList<Pet>,
    val pagination: Pagination
)