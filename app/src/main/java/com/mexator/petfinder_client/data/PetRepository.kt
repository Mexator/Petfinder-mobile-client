package com.mexator.petfinder_client.data

import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.pojo.SearchParameters
import io.reactivex.Single

interface PetRepository {
    /**
     * Set passed arguments as searched now
     */
    fun submitQuery(params: SearchParameters)

    /**
     * Return next page of current pet search
     */
    fun getPage(page: Int): Single<List<PetModel>>

    /**
     * Return observable list of photos for given pet.
     */
    fun getPetPhotos(pet: PetModel, size: PetDataSource.PhotoSize): Single<List<Drawable>>
}
