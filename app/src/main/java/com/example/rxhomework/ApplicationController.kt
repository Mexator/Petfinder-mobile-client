package com.example.rxhomework

import android.app.Application
import android.content.Context
import com.example.rxhomework.data.ActualPetRepository
import com.example.rxhomework.data.LocalDataSource
import com.example.rxhomework.data.RemoteDataSource
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.network.api_interaction.APIKeysHolder
import com.example.rxhomework.storage.StorageManager

class ApplicationController : Application() {

    companion object {
        lateinit var context: Context
        lateinit var storageManager: StorageManager
        lateinit var networkService: NetworkService
        lateinit var actualPetRepository: ActualPetRepository
        lateinit var keysHolder: APIKeysHolder
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        storageManager = StorageManager
        networkService = NetworkService
        keysHolder = APIKeysHolder(getString(R.string.API_KEY), getString(R.string.API_SECRET))
        actualPetRepository = ActualPetRepository(RemoteDataSource, LocalDataSource)
    }
}
