package com.mexator.petfinder_client.data.remote.api_interaction

import com.mexator.petfinder_client.data.remote.pojo.AnimalResponse
import com.mexator.petfinder_client.data.remote.pojo.AnimalsResponse
import com.mexator.petfinder_client.data.remote.pojo.TokenResponse
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
        @Query("type") type: String?,
        @Query("breed") breed: String?,
        @Query("page") page: Int?
    ): Single<AnimalsResponse>

    @GET("animals/{id}")
    fun getPet(@Header("Authorization") token: String,
               @Path("id") id: Long): Single<AnimalResponse>
}
