package com.mexator.petfinder_client.data.actual

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestManager
import com.mexator.petfinder_client.data.DataSource
import com.mexator.petfinder_client.data.pojo.AnimalsResponse
import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.data.pojo.PetPhotoResponse
import com.mexator.petfinder_client.network.api_interaction.APIKeysHolder
import com.mexator.petfinder_client.network.api_interaction.PetfinderJSONAPI
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject


object RemoteDataSource : DataSource<Pet>, KoinComponent {
    private val keyholder: APIKeysHolder by inject()
    private val petfinderAPI: PetfinderJSONAPI by inject()
    private val glideRM: RequestManager by inject()

    override fun getPets(animalType: String?, animalBreed: String?, page: Int): Single<List<Pet>> {

        return keyholder
            .getAccessToken()
            .flatMap { petfinderAPI.getPets("Bearer $it", animalType, animalBreed, page) }
            .map { a: AnimalsResponse -> a.animals }
    }

    override fun getPetPhotos(pet: Pet, size: DataSource.PhotoSize): Single<List<Drawable>> {
        return Single.just(pet)
            .map { it.photos }
            .map { it.map { photoResponse -> loadSinglePhoto(photoResponse, size) } }
    }

    private fun loadSinglePhoto(photoResponse: PetPhotoResponse, size: DataSource.PhotoSize)
            : Drawable {
        return glideRM.load(
            when (size) {
                DataSource.PhotoSize.SMALL -> photoResponse.small
                DataSource.PhotoSize.MEDIUM -> photoResponse.medium
                DataSource.PhotoSize.LARGE -> photoResponse.large
                else -> photoResponse.full
            }
        ).submit().get()
    }
}