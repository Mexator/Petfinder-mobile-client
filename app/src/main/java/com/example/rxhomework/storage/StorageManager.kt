package com.example.rxhomework.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.rxhomework.ApplicationController
import com.example.rxhomework.R
import com.example.rxhomework.basic_logic.SingletonHolder
import java.text.SimpleDateFormat
import java.util.*

object StorageManager {
    var tokensPreferences: SharedPreferences
        private set
    var defaultDateTimeFormat: SimpleDateFormat
        private set

    init {
        val filename = ApplicationController.context.resources.getString(R.string.tokens_file_name)
        tokensPreferences = ApplicationController.context.getSharedPreferences(filename, Context.MODE_PRIVATE)
        defaultDateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
    }
}
