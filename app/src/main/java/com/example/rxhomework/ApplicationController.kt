package com.example.rxhomework

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.rxhomework.storage_logic.StorageManager

class ApplicationController: Application() {
    companion object {
        var context: Context? = null
    }

    private lateinit var storageManager: StorageManager
    override fun onCreate() {
        super.onCreate()
        context = this
        storageManager = StorageManager(baseContext)
    }
}