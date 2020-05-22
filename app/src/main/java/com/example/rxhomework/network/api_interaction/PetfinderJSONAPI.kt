package com.example.rxhomework.network.api_interaction

import com.example.rxhomework.data.pojo.AnimalsResponse
import com.example.rxhomework.data.pojo.TokenResponse
import com.example.rxhomework.data.pojo.Breed
import com.example.rxhomework.data.pojo.Type
import io.reactivex.Single
import retrofit2.http.*

interface PetfinderJSONAPI {

    @FormUrlEncoded
    @POST("oauth2/token")
    fun getAuthToken(
        @Field("grant_type") type: String = "client_credentials",
        @Field("client_id") api_key: String,
        @Field("client_secret") api_secret: String
    ): Single<TokenResponse>

    @GET("animals")
    fun getPets(
        @Header("Authorization") token: String,
        @Query("type") type: Type?,
        @Query("breed") breed: Breed?
    ): Single<AnimalsResponse>
}
