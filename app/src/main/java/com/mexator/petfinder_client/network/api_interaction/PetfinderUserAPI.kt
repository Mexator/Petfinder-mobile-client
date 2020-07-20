package com.mexator.petfinder_client.network.api_interaction

import com.mexator.petfinder_client.data.pojo.CheckResponse
import com.mexator.petfinder_client.data.pojo.MeResponse
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
}