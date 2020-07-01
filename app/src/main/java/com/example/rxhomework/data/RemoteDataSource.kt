package com.example.rxhomework.data

import com.example.rxhomework.ApplicationController
import com.example.rxhomework.data.pojo.AnimalsResponse
import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.network.NetworkService
import io.reactivex.Single


object RemoteDataSource : DataSource {

    override fun getPets(animalType: String?, animalBreed: String?, page:Int): Single<List<Pet>> {

        return ApplicationController
            .keysHolder
            .getAccessToken()
            .flatMap { NetworkService.petfinderAPI.getPets("Bearer $it", animalType, animalBreed, page) }
            .map {a:AnimalsResponse -> a.animals }
    }
}