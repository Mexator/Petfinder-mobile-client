package com.mexator.petfinder_client.storage

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.mexator.petfinder_client.R
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object StorageManager : KoinComponent {
    private var tokensPreferences: SharedPreferences
    private var authPreferences: SharedPreferences
    private var defaultDateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
    private val appContext: Context by inject()

    init {
        val tokensFilename = appContext.resources.getString(R.string.tokens_file_name)
        tokensPreferences = appContext.getSharedPreferences(tokensFilename, Context.MODE_PRIVATE)
        val authFilename = appContext.resources.getString(R.string.auth_file_name)
        authPreferences = appContext.getSharedPreferences(authFilename, Context.MODE_PRIVATE)
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

    fun saveCredentials(userCookie: String) {
        with(authPreferences.edit()) {
            clear()
            putString("userCookie", userCookie)
            apply()
        }
    }

    fun loadCredentials(): String {
        with(authPreferences) {
            return getString("userCookie", "")!!
        }
    }

    fun writeBitmapTo(filename: String, bitmap: Bitmap): String {
        val file = File(appContext.cacheDir.absolutePath + filename)

        with(file.outputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
            flush()
            close()
        }

        return file.absolutePath
    }
}
