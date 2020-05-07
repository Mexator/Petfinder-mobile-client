package com.example.rxhomework.storage_logic

import android.content.Context
import android.content.SharedPreferences
import com.example.rxhomework.R
import com.example.rxhomework.basic_logic.SingletonHolder

class StorageManager(context: Context){
    private var tokensPrefs: SharedPreferences
    init {
        val filename = context.resources.getString(R.string.tokens_file_name)
        tokensPrefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
    }
    companion object: SingletonHolder<StorageManager, Context>(::StorageManager)
}