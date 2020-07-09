package com.example.rxhomework.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.rxhomework.ApplicationController
import com.example.rxhomework.R
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat
import java.util.*

object StorageManager : KoinComponent {
    var tokensPreferences: SharedPreferences
        private set
    var defaultDateTimeFormat: SimpleDateFormat
        private set
    private val appContext: Context by inject()

    init {
        val filename = appContext.resources.getString(R.string.tokens_file_name)
        tokensPreferences = appContext.getSharedPreferences(filename, Context.MODE_PRIVATE)
        defaultDateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
    }
}
