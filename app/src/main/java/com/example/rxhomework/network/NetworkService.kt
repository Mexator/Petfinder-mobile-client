package com.example.rxhomework.network

import com.example.rxhomework.api_interaction.PetfinderJSONAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService{
    private const val BASE_URL = "https://api.petfinder.com/v2/"
    val petfinderAPI: PetfinderJSONAPI
    private var mRetrofit: Retrofit
    init {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        petfinderAPI = mRetrofit.create(PetfinderJSONAPI::class.java)
    }
}