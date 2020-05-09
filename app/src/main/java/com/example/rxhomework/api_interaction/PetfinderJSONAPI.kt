package com.example.rxhomework.api_interaction

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.*

public interface PetfinderJSONAPI{

    @FormUrlEncoded
    @POST("oauth2/token")
    fun getAuthToken(
        @Field("grant_type") type:String = "client_credentials",
        @Field("client_id") api_key:String,
        @Field("client_secret") api_secret:String
    ): Single<JsonObject>
}