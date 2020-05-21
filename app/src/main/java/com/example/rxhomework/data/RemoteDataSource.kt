package com.example.rxhomework.data

import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.network.api_interaction.APIKeysHolder
import com.example.rxhomework.pojo.AnimalsResponse
import com.example.rxhomework.storage.PetEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import java.util.*


object RemoteDataSource : DataSource {
    override fun getPets(animalType: String?, animalBreed: String?): Single<List<PetEntity>> {

        val listType = object : TypeToken<ArrayList<PetEntity>>() {}.type
        val gson = Gson()

        return APIKeysHolder
            .getAccessToken()
            .flatMap { NetworkService.petfinderAPI.getPets("Bearer $it", animalType, animalBreed) }
            .map {a:AnimalsResponse -> a.animals }
    }
}
