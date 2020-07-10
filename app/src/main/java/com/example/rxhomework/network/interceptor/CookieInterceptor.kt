package com.example.rxhomework.network.interceptor

import com.example.rxhomework.network.api_interaction.CookieHolder
import okhttp3.Interceptor
import okhttp3.Response

class CookieInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        response.headers["Set-cookie"]?.let {
            if(it.isNotEmpty()) {
                val regex = "(?:PFSESSION=)(\\w+)(?:;)".toRegex()
                CookieHolder.userCookie = regex.find(it)!!.groupValues[1]
            }
        }
        return response
    }
}