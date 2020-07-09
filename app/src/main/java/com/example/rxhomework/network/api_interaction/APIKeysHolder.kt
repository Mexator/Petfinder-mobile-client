package com.example.rxhomework.network.api_interaction

import android.util.Log
import com.example.rxhomework.data.pojo.TokenResponse
import com.example.rxhomework.extensions.getTag
import com.example.rxhomework.storage.StorageManager
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import java.text.ParseException
import java.util.*

class APIKeysHolder(
    private val apiKey: String,
    private val apiSecret: String
) : KoinComponent {
    private val api: PetfinderJSONAPI by inject()
    private val storageManager: StorageManager by inject()

    private var accessToken: String? = null
    private var initializedIn: Date? = null
    private var expirationTime: Int = -1

    private val millisToSecs = 1e-3

    init {
        loadFromPreferences()
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

            return api
                .getAuthToken(api_key = apiKey, api_secret = apiSecret)
                .doOnSuccess { setData(it) }
                .doOnError {
                    Log.e(getTag(), it.toString())
                    if (it is HttpException && it.code() == 401) {
                        Log.wtf(
                            getTag(),
                            "Probably, you did not change values of API key and secret in secrets.xml file"
                        )
                    }
                }
                .map { it.access_token }
        } else
            return Single.just(accessToken)
    }

    private fun setData(data: TokenResponse) {
        this.accessToken = data.access_token
        this.expirationTime = data.expires_in
        this.initializedIn = Date()
        this.saveToPreferences()
    }

    private fun saveToPreferences() {
        // Save only of initialized. We don't need to store default nulls
        if (isInitialized()) {
            with(
                storageManager
                    .tokensPreferences
                    .edit()
            ) {
                clear()
                putString("access_token", accessToken)
                putString(
                    "initialized_in", storageManager
                        .defaultDateTimeFormat
                        .format(initializedIn!!)
                )
                putInt("expires_in", expirationTime)
                apply()
            }
        }
    }

    private fun loadFromPreferences() {
        val prefs = storageManager.tokensPreferences

        this.accessToken = prefs.getString("access_token", null)
        val rawDate = prefs.getString("initialized_in", "")

        try {
            this.initializedIn = storageManager
                .defaultDateTimeFormat
                .parse(rawDate!!)
        } catch (ex: ParseException) {
            this.initializedIn = null
        }

        this.expirationTime = prefs.getInt("expires_in", -1)
    }
}
