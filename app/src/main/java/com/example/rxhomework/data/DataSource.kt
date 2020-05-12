package com.example.rxhomework.data

import io.reactivex.Single

interface DataSource {
    fun getPets(): Single<List<Pet>>
}