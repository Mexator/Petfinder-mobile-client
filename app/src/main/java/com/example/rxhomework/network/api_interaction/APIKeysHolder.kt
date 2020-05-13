package com.example.rxhomework.network.api_interaction

import com.example.rxhomework.ApplicationController
import com.example.rxhomework.R
import com.example.rxhomework.network.NetworkService
import com.google.gson.JsonObject
import io.reactivex.Single
import java.text.ParseException
import java.util.*

object APIKeysHolder{
    private val TAG = APIKeysHolder.javaClass.simpleName

    private val apiKey = ApplicationController.context!!.getString(R.string.API_KEY)
    private val apiSecret = ApplicationController.context!!.getString(R.string.API_SECRET)

    private var accessToken: String? = null
    private var initializedIn: Date? = null
    private var expirationTime: Int = -1

    private val millisToSecs = 1e-3

    init {
        load()
    }

    private fun isInitialized(): Boolean {
        return (accessToken != null && initializedIn != null && expirationTime != -1)
    }

    private fun isTokenUpdateNeeded(): Boolean {
        return if (isInitialized())
            ((Date().time - initializedIn!!.time) * millisToSecs >= expirationTime)
        else true
    }

    fun getAccessToken(): Single<String> {
        if (isTokenUpdateNeeded()) {
            val apiCall =
                NetworkService
                    .petfinderAPI
                    .getAuthToken(api_key = apiKey, api_secret = apiSecret)

            val disposable = apiCall
                .subscribe { v: JsonObject ->
                    run {
                        this.accessToken = v["access_token"].asString
                        this.expirationTime = v["expires_in"].asInt
                        this.initializedIn = Date()
                        this.save()
                    }
                }

            // Return observable with string
            return apiCall.map { t: JsonObject -> t["access_token"].asString }
        } else
            return Single.just(accessToken)
    }

    private fun save() {
        // Save only of initialized. We don't need to store default nulls
        if (isInitialized()) {
            with(ApplicationController.storageManager
                .tokensPreferences
                .edit()) {
                clear()
                putString("access_token", accessToken)
                putString(
                    "initialized_in",
                    ApplicationController
                        .storageManager
                        .defaultDateTimeFormat
                        .format(initializedIn!!)
                )
                putInt("expires_in", expirationTime)
                apply()
            }
        }
    }

    private fun load() {
        val prefs = ApplicationController.storageManager.tokensPreferences

        this.accessToken = prefs.getString("access_token", null)
        val rawDate = prefs.getString("initialized_in", "")

        try {
            this.initializedIn =
                ApplicationController
                    .storageManager
                    .defaultDateTimeFormat
                    .parse(rawDate!!)
        } catch (ex: ParseException) {
            this.initializedIn = null
        }

        this.expirationTime = prefs.getInt("expires_in", -1)
    }
}
