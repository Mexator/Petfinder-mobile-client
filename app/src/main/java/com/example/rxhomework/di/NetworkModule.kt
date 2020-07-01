package com.example.rxhomework.di

import android.content.Context
import com.example.rxhomework.R
import com.example.rxhomework.data.ActualPetRepository
import com.example.rxhomework.data.LocalDataSource
import com.example.rxhomework.data.RemoteDataSource
import com.example.rxhomework.data.Repository
import com.example.rxhomework.network.api_interaction.APIKeysHolder
import org.koin.dsl.module

val networkModule = module {

    single { createAPIKeysHolder(get()) }

    single<Repository> { ActualPetRepository(RemoteDataSource, LocalDataSource) }
}

fun createAPIKeysHolder(context: Context): APIKeysHolder {
    val key = context.getString(R.string.API_KEY)
    val secret = context.getString(R.string.API_SECRET)
    return APIKeysHolder(key, secret)
}