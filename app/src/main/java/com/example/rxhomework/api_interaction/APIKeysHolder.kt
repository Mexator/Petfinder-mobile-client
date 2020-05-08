package com.example.rxhomework.api_interaction

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.example.rxhomework.ApplicationController
import com.example.rxhomework.R
import com.example.rxhomework.basic_logic.SingletonHolder
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.storage_logic.StorageManager
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class APIKeysHolder(context: Context) : LifecycleObserver {
    private val apiKey: String
    private val apiSecret: String

    lateinit var accessToken: String
        private set

    private lateinit var initializedIn: Date
    private var expirationTime: Int = -1

    private val millisToSecs = 1 / 1000

    init {
        apiKey = ApplicationController.context!!.getString(R.string.API_KEY)
        apiSecret = ApplicationController.context!!.getString(R.string.API_SECRET)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun updateTokens() {
        //Try to read access data from disk. If fail - perform auth request
        val tokenPrefs = StorageManager.
        getInstance(ApplicationController.context!!)
            .tokensPrefs

        val rawToken = tokenPrefs.getString("access_token", null)
        val rawDate = tokenPrefs.getString("initialized_in", null)
        val rawExpirationTime = tokenPrefs.getInt("expires_in", -1)

        if (rawToken == null ||
            rawDate == null ||
            rawExpirationTime == -1
        ) {
            NetworkService
                .petfinderAPI
                .getAuthToken(
                    api_key = apiKey,
                    api_secret = apiSecret
                ).enqueue(object : Callback<JsonElement> {
                    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                        val body = response.body()!!.asJsonObject
                        accessToken = body["access_token"].asString
                        expirationTime = body["expires_in"].asInt
                        initializedIn = Date()
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        } else {
            val preParsed =
                StorageManager.getInstance(ApplicationController.context!!).defaultDateTimeFormat.parse(rawDate)!!
            if ((Date().time - preParsed.time) * millisToSecs < rawExpirationTime) {
                NetworkService
                    .petfinderAPI
                    .getAuthToken(
                        api_key = apiKey,
                        api_secret = apiSecret
                    ).enqueue(object : Callback<JsonElement> {
                        override fun onResponse(
                            call: Call<JsonElement>,
                            response: Response<JsonElement>
                        ) {
                            val body = response.body()!!.asJsonObject
                            accessToken = body["access_token"].asString
                            expirationTime = body["expires_in"].asInt
                            initializedIn = Date()
                        }

                        override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
            } else {
                accessToken = rawToken!!
                initializedIn =
                    StorageManager.getInstance(ApplicationController.context!!).defaultDateTimeFormat.parse(rawDate)!!
                expirationTime = rawExpirationTime
            }
        }

    }

    private var activity:Activity?=null
    fun activityRecognitionHelper(lifecycleOwner: LifecycleOwner, activity: Activity){
        this.activity = activity;
        lifecycleOwner.lifecycle.addObserver(this);
    }

    companion object : SingletonHolder<APIKeysHolder, Context>(::APIKeysHolder)
}