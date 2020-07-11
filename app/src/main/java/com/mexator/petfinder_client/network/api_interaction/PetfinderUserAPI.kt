package com.mexator.petfinder_client.network.api_interaction

import com.mexator.petfinder_client.data.pojo.CheckResponse
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PetfinderUserAPI {
    @Headers("X-Requested-With: XMLHttpRequest")
    @POST("user/login_check/")
    //Content-Type: multipart/form-data; boundary=bound
    fun checkLogin(
        @Body formData: RequestBody
    ): Single<CheckResponse>
}