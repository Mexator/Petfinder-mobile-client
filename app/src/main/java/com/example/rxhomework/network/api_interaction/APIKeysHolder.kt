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

    /**
     * The function returns Single parameterized with String. The API access token can be obtained
     * from it.
     * Single -- Observable from RxJava library, that emits one value and then finishes.
     * http://reactivex.io/documentation/single.html
     * Just like Observable, you can subscribe on it by calling subscribe() method. Two lambdas
     * should be passed there: OnSuccess action, and OnError action (second can be omitted, but it is
     * a bad practise to do so). Single has no OnNext handler, and its only value can be obtained with
     * OnSuccess.
     * So, if you want to get the key - just subscribe on it. Probably, you want to do it in
     * separate thread, then you could use subscribeOn and ObserveOn
     * If you want to use in other reactive stream, check this link
     * https://openclassrooms.com/en/courses/4788266-integrate-remote-data-into-your-app/5293916-chaining-different-network-queries-with-rxjava
     */
    fun getAccessToken(): Single<String> {
        if (isTokenUpdateNeeded()) {
            //TODO: DISPOSABLE PRODUCE MEMORY LEAK!!!
            val apiCall =
                NetworkService
                    .petfinderAPI
                    .getAuthToken(api_key = apiKey, api_secret = apiSecret)

            // Subscribe here to update class members and save to dis
            val disposable = apiCall
                .subscribe { v: JsonObject ->
                    run {
                        this.accessToken = v["access_token"].toString()
                        this.expirationTime = v["expires_in"].asInt
                        this.initializedIn = Date()
                        this.save()
                    }
                }

            // Return observable with string
            return apiCall.map { t: JsonObject -> t["access_token"].toString() }
        } else
            return Single.just(accessToken)
    }

    private fun save() {
        // Save only of initialized. We don't need to store default nulls
        if (isInitialized()) {
            ApplicationController.storageManager
                .tokensPreferences
                .edit()
                .putString("access_token", accessToken)
                .putString(
                    "initialized_in",
                    ApplicationController
                        .storageManager
                        .defaultDateTimeFormat
                        .format(initializedIn!!)
                )
                .putInt("expires_in", expirationTime)
                .apply()
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
