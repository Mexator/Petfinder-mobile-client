package com.mexator.petfinder_client.data.remote.pojo

data class FavoriteResponse (
    val favorites: List<Favorite>,
    val pagination: Pagination
)