package com.example.rxhomework.data

import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.network.api_interaction.APIKeysHolder
import com.example.rxhomework.storage.PetEntity
import io.reactivex.Single

class RemoteDataSource : DataSource{
    override fun getPets(animalType: String?, animalBreed: String?): Single<List<PetEntity>> {
//        return APIKeysHolder
//            .getAccessToken()
//            .toObservable()
//            .flatMap { NetworkService.petfinderAPI.getPets(it, animalType, animalBreed) }
//            .map {  }
        TODO()
    }
}
