package com.example.rxhomework.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

private val CONTENT_TYPE_HEADER = "Accept"
private val CHARSET_HEADER = "Accept-Charset"

class HeaderInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        builder.addHeader(CONTENT_TYPE_HEADER, "text/plain")
        builder.addHeader(CHARSET_HEADER, "utf-8")

        return chain.proceed(builder.build())
    }

}