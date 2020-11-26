package com.mexator.petfinder_client.data.remote.api_interaction

import com.mexator.petfinder_client.data.remote.pojo.CheckResponse
import com.mexator.petfinder_client.data.remote.pojo.FavoriteResponse
import com.mexator.petfinder_client.data.remote.pojo.MeResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface PetfinderUserAPI {
    @Headers("X-Requested-With: XMLHttpRequest")
    @POST("user/login_check/")
    fun checkLogin(
        @Body formData: RequestBody
    ): Single<CheckResponse>

    @GET("/user/me/")
    fun getMe(@Header("Cookie") sessionCookie: String): Single<MeResponse>

    @GET("/v2/me/favorites")
    fun getFavorites(@Header("Cookie") sessionCookie: String): Single<FavoriteResponse>

    @Headers("Content-type: application/json")
    @POST("/v2/me/favorites/")
    fun addToFavorites(
        @Header("Cookie") sessionCookie: String,
        @Body favorite: RequestBody): Completable

    @DELETE("/v2/me/favorites/{id}")
    fun deleteFromFavorites(
        @Path("id") petId: Long,
        @Header("Cookie") sessionCookie: String): Completable
}