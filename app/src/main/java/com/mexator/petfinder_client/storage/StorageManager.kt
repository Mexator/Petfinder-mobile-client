package com.mexator.petfinder_client.storage

import android.content.Context
import android.content.SharedPreferences
import com.mexator.petfinder_client.R
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat
import java.util.*

object StorageManager : KoinComponent {
    private var tokensPreferences: SharedPreferences
    private var defaultDateTimeFormat: SimpleDateFormat
    private val appContext: Context by inject()

    init {
        val filename = appContext.resources.getString(R.string.tokens_file_name)
        tokensPreferences = appContext.getSharedPreferences(filename, Context.MODE_PRIVATE)
        defaultDateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
    }

    fun saveAPIKeys(token: String, initializedIn: Date, expiresIn: Int) {
        with(tokensPreferences.edit()) {
            clear()
            putString("access_token", token)
            putString("initialized_in", defaultDateTimeFormat.format(initializedIn))
            putInt("expires_in", expiresIn)
            apply()
        }
    }

    fun loadAPIKeys(): Triple<String?, Date?, Int> {
        val accessToken = tokensPreferences.getString("access_token", null)
        val rawDate = tokensPreferences.getString("initialized_in", null)

        val date = rawDate?.let {
            defaultDateTimeFormat
                .parse(it)
        }

        val expirationTime = tokensPreferences.getInt("expires_in", -1)
        return Triple(accessToken, date, expirationTime)
    }
}
