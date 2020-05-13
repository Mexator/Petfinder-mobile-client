package com.example.rxhomework.network.api_interaction

import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.Type
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

public interface PetfinderJSONAPI {

    @FormUrlEncoded
    @POST("oauth2/token")
    fun getAuthToken(
        @Field("grant_type") type: String = "client_credentials",
        @Field("client_id") api_key: String,
        @Field("client_secret") api_secret: String
    ): Single<JsonObject>

    @GET("animals")
    fun getPets(
        @Header("Authorization") token: String,
        @Query("type") type: Type?,
        @Query("breed") breed: Breed?
    ): Observable<JsonObject>
}
