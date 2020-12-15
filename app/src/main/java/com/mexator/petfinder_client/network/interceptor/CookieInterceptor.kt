package com.mexator.petfinder_client.network.interceptor

import android.util.Log
import com.mexator.petfinder_client.data.remote.api_interaction.CookieHolder
import okhttp3.Interceptor
import okhttp3.Response

class CookieInterceptor: Interceptor {
    val TAG = "CookieInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        response.headers["Set-cookie"]?.let { it ->
            if(it.isNotEmpty()) {
                val regex = "(?:PFSESSION=)(\\w+)(?:;)".toRegex()
                regex.find(it)?.groupValues?.get(1)?.let {
                        t -> CookieHolder.userCookie = t
                        Log.d(TAG, "Intercepted cookie: $t")
                }
            }
        }
        return response
    }
}