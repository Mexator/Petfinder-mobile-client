package com.example.rxhomework

import android.app.Application
import android.content.Context
import com.example.rxhomework.storage.StorageManager

class ApplicationController : Application() {
    companion object {
        lateinit var context: Context
        lateinit var storageManager: StorageManager
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        storageManager = StorageManager(baseContext)
    }
}
