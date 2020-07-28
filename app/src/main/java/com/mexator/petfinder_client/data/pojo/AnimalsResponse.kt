package com.mexator.petfinder_client.data.pojo

data class AnimalsResponse(
    val animals: ArrayList<PetResponse>,
    val pagination: Pagination
)
