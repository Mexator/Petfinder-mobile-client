package com.mexator.petfinder_client.storage

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.mexator.petfinder_client.R
import com.mexator.petfinder_client.storage.StorageManager.tokensPreferences
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * [StorageManager] is an object that is used to handle all operations related to reading and writing
 * data that should be kept between restarts of the app to the local disk storage.
 *
 * For example, it can be some API token that can
 * be reused between restarts.
 *
 * Usually, all data is written to [SharedPreferences]
 * @property tokensPreferences [SharedPreferences] instance where Petfinder API access token is stored
 * @property authPreferences [SharedPreferences] instance where Petfinder **user** API access token is stored
 * @property defaultDateTimeFormat used to parse Date from string and vice versa when reading/writing
 */
object StorageManager : KoinComponent {
    // Dependencies
    private val appContext: Context by inject()

    private var tokensPreferences: SharedPreferences
    private var authPreferences: SharedPreferences
    private var defaultDateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)

    init {
        val tokensFilename = appContext.resources.getString(R.string.tokens_file_name)
        tokensPreferences = appContext.getSharedPreferences(tokensFilename, Context.MODE_PRIVATE)
        val authFilename = appContext.resources.getString(R.string.auth_file_name)
        authPreferences = appContext.getSharedPreferences(authFilename, Context.MODE_PRIVATE)
    }

    /**
     * [saveAPIKeys] is used to save API token, and data for checking if it is expired to the [tokensPreferences]
     *
     * @param token API token of Petfinder API
     * @param initializedIn date and time when the token was given
     * @param expiresIn TTL of the token (in seconds)
     */
    fun saveAPIKeys(token: String, initializedIn: Date, expiresIn: Int) {
        with(tokensPreferences.edit()) {
            clear()
            putString("access_token", token)
            putString("initialized_in", defaultDateTimeFormat.format(initializedIn))
            putInt("expires_in", expiresIn)
            apply()
        }
    }

    /**
     * [loadAPIKeys] is used to load data saved by [saveAPIKeys] from [tokensPreferences]
     *
     * @see [saveAPIKeys]
     */
    fun loadAPIKeys(): Triple<String?, Date?, Int> {
        with(tokensPreferences) {
        val accessToken = getString("access_token", null)
        val date = getString("initialized_in", null)
            ?.let { defaultDateTimeFormat.parse(it) }
        val expirationTime = getInt("expires_in", -1)
        return Triple(accessToken, date, expirationTime)
        }
    }

    /**
     * [saveCredentials] save access token of Petfinder user API to [authPreferences]
     * @param userCookie Access cookie of user
     */
    fun saveCredentials(userCookie: String) {
        with(authPreferences.edit()) {
            clear()
            putString("userCookie", userCookie)
            apply()
        }
    }

    /**
     * [loadCredentials] is used to load data saved by [saveCredentials] from [authPreferences]
     */
    fun loadCredentials(): String {
        with(authPreferences) {
            return getString("userCookie", "")!!
        }
    }

    /**
     * Save [bitmap] as file with name [filename] to the application cache directory
     *
     * @param filename name for file
     * @param bitmap content of bitmap
     * @return absolute path to the saved file
     * @see Context.getCacheDir
     */
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
