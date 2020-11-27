package com.mexator.petfinder_client.data

import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * [PetRepository] is a Repository that is responsible for searching pets
 * independently on current user.
 *
 * Usage model:
 * 1. Set search parameters via submitQuery()
 * 2. Use getPage() to obtain paginated pet list.
 * 3. For each pet get preview and/or its photos with getPetPhotos() and
 * getPetPreview() methods
 */
interface PetRepository {
    /**
     * Set passed search [params] as searched now
     */
    fun submitQuery(params: SearchParameters)

    /**
     * Get next page of current pet search
     * @return list of pets whose parameters satisfy the query params
     */
    fun getPage(page: Int): Single<List<PetModel>>

    /**
     * Return observable list of photos of given pet.
     */
    fun getPetPhotos(pet: PetModel, size: PetDataSource.PhotoSize): List<Single<Drawable>>

    /**
     * @return preview photo of [pet] or `null` if it has no photos.
     */
    fun getPetPreview(pet: PetModel): Maybe<Drawable>
}
