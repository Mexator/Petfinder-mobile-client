package com.example.rxhomework.data

import com.example.rxhomework.data.pojo.AnimalsResponse
import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.network.api_interaction.APIKeysHolder
import io.reactivex.Single


object RemoteDataSource : DataSource {
    override fun getPets(animalType: String?, animalBreed: String?): Single<List<Pet>> {


        return APIKeysHolder
            .getAccessToken()
            .flatMap { NetworkService.petfinderAPI.getPets("Bearer $it", animalType, animalBreed) }
            .map {a:AnimalsResponse -> a.animals }
    }
}
