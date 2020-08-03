package com.mexator.petfinder_client.data

import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.model.PetModel
import io.reactivex.Single

interface PetRepository {
    fun getPets(
        animalType: String? = null,
        animalBreed: String? = null,
        page: Int? = 1
    ): Single<List<PetModel>>

    fun getPetPhotos(
        pet: PetModel,
        size: DataSource.PhotoSize
    ): Single<List<Drawable>>
}
