package com.mexator.petfinder_client.data

import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import io.reactivex.Single

/**
 * DataSources are classes used by Repositories to obtain data from.
 * Example of DataSources: DatabaseDataSource, APIDataSource, etc.
 */
interface PetDataSource<PM : PetModel> {
    fun getPets(parameters: SearchParameters, page: Int): Single<List<PM>>

    fun getPetPhotos(pet: PM, size: PhotoSize): Single<List<Drawable>>

    enum class PhotoSize { SMALL, MEDIUM, LARGE, FULL }
}
