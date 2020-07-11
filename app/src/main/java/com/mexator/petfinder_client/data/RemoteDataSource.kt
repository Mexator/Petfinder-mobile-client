package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.pojo.AnimalsResponse
import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.network.api_interaction.APIKeysHolder
import com.mexator.petfinder_client.network.api_interaction.PetfinderJSONAPI
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject


object RemoteDataSource : DataSource, KoinComponent {
    private val keyholder: APIKeysHolder by inject()
    private val petfinderAPI: PetfinderJSONAPI by inject()

    override fun getPets(animalType: String?, animalBreed: String?, page: Int): Single<List<Pet>> {

        return keyholder
            .getAccessToken()
            .flatMap { petfinderAPI.getPets("Bearer $it", animalType, animalBreed, page) }
            .map { a: AnimalsResponse -> a.animals }
    }
}