package com.example.rxhomework.di

import android.content.Context
import com.example.rxhomework.R
import com.example.rxhomework.data.ActualPetRepository
import com.example.rxhomework.data.LocalDataSource
import com.example.rxhomework.data.RemoteDataSource
import com.example.rxhomework.data.Repository
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.network.api_interaction.APIKeysHolder
import com.example.rxhomework.network.api_interaction.PetfinderJSONAPI
import com.example.rxhomework.utils.HtmlDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.petfinder.com/v2/"

val networkModule = module {

    single { createAPIKeysHolder(get()) }

    single { NetworkService() }

    single { createLoggingInterceptor() }

    single { createClient(listOf(get<HttpLoggingInterceptor>())) }

    single { createGson() }

    single<Converter.Factory> { createConverterFactory(get()) }

    single<CallAdapter.Factory> { RxJava2CallAdapterFactory.createAsync() }

    single<Retrofit> { createRetrofit(get(), get(), get()) }

    single { get<Retrofit>().create(PetfinderJSONAPI::class.java) }

    single<Repository> { ActualPetRepository(RemoteDataSource, LocalDataSource) }
}

fun createLoggingInterceptor() = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BASIC)

fun createClient(interceptors: List<Interceptor>): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()

    for (interceptor in interceptors) {
        clientBuilder.addInterceptor(interceptor)
    }
    return clientBuilder.build()
}

fun createGson(): Gson = GsonBuilder()
    .registerTypeAdapter(String::class.java, HtmlDeserializer())
    .create()

fun createConverterFactory(gson: Gson): GsonConverterFactory =
    GsonConverterFactory.create(gson)

fun createRetrofit(
    client: OkHttpClient,
    callAdapterFactory: CallAdapter.Factory,
    converterFactory: Converter.Factory
): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()

fun createAPIKeysHolder(context: Context): APIKeysHolder {
    val key = context.getString(R.string.API_KEY)
    val secret = context.getString(R.string.API_SECRET)
    return APIKeysHolder(key, secret)
}
