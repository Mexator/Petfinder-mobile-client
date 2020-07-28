package com.mexator.petfinder_client.data

import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.model.PetModel
import io.reactivex.Single

interface DataSource<PM : PetModel> {
    fun getPets(
        animalType: String? = null,
        animalBreed: String? = null,
        page: Int = 1
    ): Single<List<PM>>

    fun getPetPhotos(pet: PM, size: PhotoSize): Single<List<Drawable>>

    enum class PhotoSize { SMALL, MEDIUM, LARGE, FULL }
}
