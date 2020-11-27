package com.mexator.petfinder_client.data.remote.pojo

import com.google.gson.annotations.SerializedName
import com.mexator.petfinder_client.data.model.User

data class MeResponse (
    val user: User,
    @SerializedName("api_passthrough_token")
    val apiPassToken: String
)