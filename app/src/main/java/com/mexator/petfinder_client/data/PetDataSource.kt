package com.mexator.petfinder_client.data

import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * PetDataSource is used to get info about pets, that can be obtained
 * **without** knowing of user account
 *
 * DataSources are classes used by Repositories to obtain data from.
 * Example of DataSources: DatabaseDataSource, APIDataSource, etc.
 */
interface PetDataSource<PM : PetModel> {
    fun getPets(parameters: SearchParameters, page: Int): Single<List<PM>>
    fun getPet(id: Long): Maybe<PM>

    fun getPetPhotos(pet: PM, size: PhotoSize): List<Single<Drawable>>
    fun getPetPreview(pet: PM): Maybe<Drawable>

    enum class PhotoSize { SMALL, MEDIUM, LARGE, FULL }
}
