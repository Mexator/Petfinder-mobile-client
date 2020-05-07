package com.example.rxhomework.storage_logic

import android.content.Context
import android.content.SharedPreferences
import com.example.rxhomework.R
import com.example.rxhomework.basic_logic.SingletonHolder
import java.text.SimpleDateFormat
import java.util.*

class StorageManager(context: Context){
    var tokensPrefs: SharedPreferences
        private set
    var defaultDateTimeFormat:SimpleDateFormat
        private set

    init {
        val filename = context.resources.getString(R.string.tokens_file_name)
        tokensPrefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
        defaultDateTimeFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US)
    }
    companion object: SingletonHolder<StorageManager, Context>(::StorageManager)
}