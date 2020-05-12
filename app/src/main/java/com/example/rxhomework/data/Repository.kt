package com.example.rxhomework.data

import io.reactivex.Single

interface Repository {
    fun getPets(): Single<List<Pet>>
}