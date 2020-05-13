package com.example.rxhomework

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.rxhomework.data.LocalDataSource
import com.example.rxhomework.data.PetRepository
import com.example.rxhomework.data.RemoteDataSource
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.storage.StorageManager

class ApplicationController : Application() {
    private val TAG = ApplicationController::class.java.toString()

    companion object {
        lateinit var context: Context
        lateinit var storageManager: StorageManager
        lateinit var networkService: NetworkService
        lateinit var petRepository: PetRepository
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        storageManager = StorageManager
        networkService = NetworkService
        petRepository = PetRepository(RemoteDataSource(), LocalDataSource())
    }
}