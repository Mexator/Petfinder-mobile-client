package com.example.rxhomework.data

import com.example.rxhomework.ApplicationController
import com.example.rxhomework.network.NetworkService
import io.reactivex.Observable
import io.reactivex.Single

class PetRepository: Repository {
    private val remoteDataSource:DataSource?=null
    private val localDataSource:DataSource?=null

    override fun getPets(): Single<List<Pet>> {
        return NetworkService
            .isConnectedToInternet()
            .flatMap { it -> if(it) remoteDataSource?.getPets() else localDataSource?.getPets() }
    }
}