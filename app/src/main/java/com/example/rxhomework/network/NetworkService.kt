package com.example.rxhomework.network

import android.util.Log
import com.example.rxhomework.network.api_interaction.PetfinderJSONAPI
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
    init {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
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
            .doOnError { it -> Log.i(TAG,it.toString()) }
            .onErrorReturnItem("Error")
            .map { it != "Error" }
    }
}