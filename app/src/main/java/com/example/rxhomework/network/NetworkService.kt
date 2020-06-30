package com.example.rxhomework.network

import com.example.rxhomework.network.api_interaction.PetfinderJSONAPI
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private val TAG = NetworkService.javaClass.simpleName

    private const val BASE_URL = "https://api.petfinder.com/v2/"
    private const val INTERNET_CHECK_URL = "google.com"
    private const val TIMEOUT = 5L

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
        // Just check whether we can ping google (our API endpoint does not respond to ping requests)
        val command = "ping -c 1 -W $TIMEOUT $INTERNET_CHECK_URL";
        return Single
            .defer{Single.just(Runtime.getRuntime().exec(command).waitFor())}
            .subscribeOn(Schedulers.io())
            // Trick to map an error to just a false value
            .onErrorReturnItem(-1)
            .map { it == 0 }
    }
}