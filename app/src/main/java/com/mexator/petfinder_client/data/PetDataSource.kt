package com.mexator.petfinder_client.data

import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * [PetDataSource] is used to get info about pets, that can be obtained
 * **without** knowing of user account
 *
 * DataSources are classes used by Repositories to obtain data from. Usually
 * they should not be used by any class that is not a [PetRepository]
 */
interface PetDataSource<PM : PetModel> {
    /**
     *  Get list of pets whose parameters satisfy query
     */
    fun getPets(parameters: SearchParameters, page: Int): Single<List<PM>>

    /**
     * Get pet by ID, or `null` if pet with this ID is not present
     */
    fun getPet(id: Long): Maybe<PM>

    /**
     * Get photos of [pet] from storage
     */
    fun getPetPhotos(pet: PM, size: PhotoSize): List<Single<Drawable>>

    /**
     * Get preview image of the pet
     */
    fun getPetPreview(pet: PM): Maybe<Drawable>

    /**
     * The [PhotoSize] enum is used to pass requested size of photo in
     * [getPetPhotos] function
     */
    enum class PhotoSize { SMALL, MEDIUM, LARGE, FULL }
}
