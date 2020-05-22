package com.example.rxhomework.network.api_interaction

import android.util.Log
import com.example.rxhomework.ApplicationController
import com.example.rxhomework.R
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.data.pojo.TokenResponse
import io.reactivex.Single
import retrofit2.HttpException
import java.text.ParseException
import java.util.*

object APIKeysHolder {
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
                .subscribe({ v: TokenResponse ->
                    run {
                        this.accessToken = v.access_token
                        this.expirationTime = v.expires_in
                        this.initializedIn = Date()
                        this.save()
                    }
                }, { e ->
                    Log.e(TAG, e.toString())
                    if (e is HttpException && e.code() == 401) {
                        Log.wtf(
                            TAG,
                            "Probably, you did not change values of API key and secret in secrets.xml file"
                        )
                    }
                }
                )

            // Return observable with string
            return apiCall.map { t: TokenResponse -> t.access_token }
        } else
            return Single.just(accessToken)
    }

    private fun save() {
        // Save only of initialized. We don't need to store default nulls
        if (isInitialized()) {
            with(
                ApplicationController.storageManager
                    .tokensPreferences
                    .edit()
            ) {
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
