package com.mexator.petfinder_client.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class Favorite(
    @SerializedName("animal_id")
    val id: Int
)