package com.example.rxhomework

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.rxhomework.data.LocalDataSource
import com.example.rxhomework.data.PetRepository
import com.example.rxhomework.data.RemoteDataSource
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.network.api_interaction.APIKeysHolder
import com.example.rxhomework.storage.PetEntity
import com.example.rxhomework.storage.StorageManager
import io.reactivex.schedulers.Schedulers

class ApplicationController : Application() {
    private val TAG = ApplicationController::class.java.toString()

    companion object {
        lateinit var context: Context
        lateinit var storageManager: StorageManager
        lateinit var networkService: NetworkService
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        storageManager = StorageManager
        networkService = NetworkService

        val petrepo = PetRepository(RemoteDataSource, LocalDataSource)
        val disp = petrepo.getPets()
            .subscribe(
                {Log.i(TAG,it.size.toString())},
                {Log.i(TAG,it.toString())}
        )
    }
}
