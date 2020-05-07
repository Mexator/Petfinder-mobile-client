package com.example.rxhomework.api_interaction

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.rxhomework.basic_logic.SingletonHolder
import com.example.rxhomework.storage_logic.StorageManager
import java.util.*

class APIKeysHolder(context: Context) : LifecycleObserver {
    lateinit var accessToken: String
        private set

    private lateinit var initializedIn: Date
    private var expirationTime: Int = -1

    private val millisToSecs = 1/1000

    init {
        updateTokens(context)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun updateTokens(context: Context) {
        //Try to read access data from disk. If fail - perform auth request
        val tokenPrefs = StorageManager.getInstance(context).tokensPrefs

        val rawToken = tokenPrefs.getString("access_token", null)
        val rawDate = tokenPrefs.getString("initialized_in", null)
        val rawExpirationTime = tokenPrefs.getInt("expires_in", -1)

        if (rawToken == null ||
            rawDate == null ||
            rawExpirationTime == -1
        ) {
            //TODO:Perform auth request
        } else {
            val preParsed =
                StorageManager.getInstance(context).defaultDateTimeFormat.parse(rawDate)!!
            if ((Date().time - preParsed.time)*millisToSecs < rawExpirationTime) {
                //TODO:Perform auth request
            }
        }

        accessToken = rawToken!!
        initializedIn = StorageManager.getInstance(context).defaultDateTimeFormat.parse(rawDate!!)!!
        expirationTime = rawExpirationTime
    }

    companion object : SingletonHolder<APIKeysHolder, Context>(::APIKeysHolder)
}