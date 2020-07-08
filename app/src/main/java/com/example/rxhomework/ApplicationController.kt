package com.example.rxhomework

import android.app.Application
import android.content.Context
import com.example.rxhomework.data.ActualPetRepository
import com.example.rxhomework.data.LocalDataSource
import com.example.rxhomework.data.RemoteDataSource
import com.example.rxhomework.di.networkModule
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.network.api_interaction.APIKeysHolder
import com.example.rxhomework.storage.StorageManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationController : Application() {

    companion object {
        lateinit var context: Context
        lateinit var storageManager: StorageManager
        lateinit var actualPetRepository: ActualPetRepository
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        storageManager = StorageManager
        actualPetRepository = ActualPetRepository(RemoteDataSource, LocalDataSource)

        startKoin {
            androidContext(this@ApplicationController)

            modules(listOf(networkModule))
        }
    }
}
