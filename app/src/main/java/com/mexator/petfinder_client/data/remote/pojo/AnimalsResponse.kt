package com.mexator.petfinder_client.data.remote.pojo

data class AnimalsResponse(
    val animals: ArrayList<PetResponse>,
    val pagination: Pagination
)
