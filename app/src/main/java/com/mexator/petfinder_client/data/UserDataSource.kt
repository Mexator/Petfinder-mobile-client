package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.model.User
import io.reactivex.Completable
import io.reactivex.Single

/**
 * [UserDataSource] is a data source used by [UserDataRepository]
 */
interface UserDataSource {

    /**
     * Get user model instance using authentication [cookie][userCookie]
     */
    fun getUser(userCookie: String): Single<User>

    /**
     * Get list of favorite pets
     */
    fun getFavorites(userCookie: String): Single<List<PetModel>>

    /**
     * Get list of favorite pets' IDs
     */
    fun getFavoritesIDs(userCookie: String): Single<List<Long>>

    /**
     * Add pet to list of favorites
     */
    fun addFavorite(userCookie: String, pet: PetModel): Completable

    /**
     * Remove pet from list of favorites
     */
    fun removeFavorite(userCookie: String, pet: PetModel): Completable
}