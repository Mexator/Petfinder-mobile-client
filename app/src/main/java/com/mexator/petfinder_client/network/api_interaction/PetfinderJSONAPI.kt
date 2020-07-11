package com.mexator.petfinder_client.network.api_interaction

import com.mexator.petfinder_client.data.pojo.AnimalsResponse
import com.mexator.petfinder_client.data.pojo.TokenResponse
import com.mexator.petfinder_client.storage.Breed
import com.mexator.petfinder_client.storage.Type
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
        @Query("breed") breed: Breed?,
        @Query("page") page: Int?
    ): Single<AnimalsResponse>
}