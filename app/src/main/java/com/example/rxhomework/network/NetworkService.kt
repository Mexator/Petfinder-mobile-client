package com.example.rxhomework.network

import com.example.rxhomework.network.api_interaction.PetfinderJSONAPI
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress

object NetworkService {
    private val TAG = NetworkService::class.java.toString()

    private const val BASE_URL = "https://api.petfinder.com/v2/"
    private const val INTERNET_CHECK_URL = "petfinder.com"

    val petfinderAPI: PetfinderJSONAPI
    private var mRetrofit: Retrofit
    private var queryLogger: HttpLoggingInterceptor = HttpLoggingInterceptor()

    init {
        queryLogger.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
        client.addInterceptor(queryLogger)

        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()
        petfinderAPI = mRetrofit.create(PetfinderJSONAPI::class.java)
    }

    fun isConnectedToInternet(): Single<Boolean> {
        // Just check whether we can resolve IP for our target API endpoint
        return Single
            .defer{Single.just(InetAddress.getByName(INTERNET_CHECK_URL).toString())}
            .subscribeOn(Schedulers.io())
            // Trick to map an error to just a false value
            .onErrorReturnItem("Error")
            .map { it != "Error" }
    }
}