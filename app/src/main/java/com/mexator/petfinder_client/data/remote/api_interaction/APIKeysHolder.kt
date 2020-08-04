package com.mexator.petfinder_client.data.remote.api_interaction

import android.util.Log
import com.mexator.petfinder_client.data.remote.pojo.TokenResponse
import com.mexator.petfinder_client.extensions.getTag
import com.mexator.petfinder_client.storage.StorageManager
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
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
        with(storageManager.loadAPIKeys()) {
            accessToken = first
            initializedIn = second
            expirationTime = third
        }
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
        storageManager.saveAPIKeys(accessToken!!, initializedIn!!, expirationTime)
    }
}
