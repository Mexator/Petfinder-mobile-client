package com.mexator.petfinder_client.data.actual

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestManager
import com.mexator.petfinder_client.data.PetDataSource
import com.mexator.petfinder_client.data.remote.api_interaction.APIKeysHolder
import com.mexator.petfinder_client.data.remote.api_interaction.PetfinderJSONAPI
import com.mexator.petfinder_client.data.remote.pojo.AnimalsResponse
import com.mexator.petfinder_client.data.remote.pojo.PetPhotoResponse
import com.mexator.petfinder_client.data.remote.pojo.PetResponse
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import io.reactivex.Maybe
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject


object RemotePetDataSource : PetDataSource<PetResponse>, KoinComponent {
    private val keyholder: APIKeysHolder by inject()
    private val petfinderAPI: PetfinderJSONAPI by inject()
    private val glideRM: RequestManager by inject()

    override fun getPets(parameters: SearchParameters, page: Int): Single<List<PetResponse>> {
        return keyholder
            .getAccessToken()
            .flatMap {
                petfinderAPI.getPets(
                    "Bearer $it",
                    parameters.animalType,
                    parameters.animalBreed,
                    page
                )
            }
            .map { a: AnimalsResponse -> a.animals }
    }

    override fun getPetPhotos(
        pet: PetResponse,
        size: PetDataSource.PhotoSize
    ): Single<List<Drawable>> {
        return Single.just(pet)
            .map { it.photos ?: emptyList() }
            .map { it.map { photoResponse -> loadSinglePhoto(photoResponse, size) } }
    }

    override fun getPetPreview(pet: PetResponse): Maybe<Drawable> =
        if (pet.photos.isNullOrEmpty())
            Maybe.empty()
        else
            Maybe.just(pet.photos[0])
                .map { loadSinglePhoto(it, PetDataSource.PhotoSize.SMALL) }


    private fun loadSinglePhoto(photoResponse: PetPhotoResponse, size: PetDataSource.PhotoSize)
            : Drawable {
        return glideRM.load(
            when (size) {
                PetDataSource.PhotoSize.SMALL -> photoResponse.small
                PetDataSource.PhotoSize.MEDIUM -> photoResponse.medium
                PetDataSource.PhotoSize.LARGE -> photoResponse.large
                else -> photoResponse.full
            }
        ).submit().get()
    }
}