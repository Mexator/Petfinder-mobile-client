package com.example.rxhomework

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.example.rxhomework.storage_logic.StorageManager

class ApplicationController: Application() {
    private lateinit var storageManager: StorageManager
    override fun onCreate() {
        super.onCreate()
        storageManager = StorageManager(baseContext)
    }
}