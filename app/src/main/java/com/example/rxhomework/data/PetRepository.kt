package com.example.rxhomework.data

import com.example.rxhomework.ApplicationController
import com.example.rxhomework.network.NetworkService
import io.reactivex.Observable
import io.reactivex.Single

class PetRepository: Repository {
    private val remoteDataSource:DataSource = object : DataSource {
        override fun getPets(): Single<List<Pet>> {
            val pets = ArrayList<Pet>()
            val pet = Pet("remotePet")
            pets.add(pet)
            return Single.just(pets)
        }
    }
    private val localDataSource:DataSource = object : DataSource {
        override fun getPets(): Single<List<Pet>> {
            val pets = ArrayList<Pet>()
            val pet = Pet("localPet")
            pets.add(pet)
            return Single.just(pets)
        }
    }

    override fun getPets(): Single<List<Pet>> {
        return NetworkService
            .isConnectedToInternet()
            .flatMap { it -> if(it) remoteDataSource.getPets() else localDataSource.getPets() }
    }
}