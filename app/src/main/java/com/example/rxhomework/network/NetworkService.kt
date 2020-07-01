package com.example.rxhomework.network

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

private const val INTERNET_CHECK_URL = "google.com"
private const val TIMEOUT = 5L

class NetworkService {
    fun isConnectedToInternet(): Single<Boolean> {
        // Just check whether we can ping google (our API endpoint does not respond to ping requests)
        val command = "ping -c 1 -W $TIMEOUT $INTERNET_CHECK_URL";
        return Single
            .defer { Single.just(Runtime.getRuntime().exec(command).waitFor()) }
            .subscribeOn(Schedulers.io())
            // Trick to map an error to just a false value
            .onErrorReturnItem(-1)
            .map { it == 0 }
    }
}