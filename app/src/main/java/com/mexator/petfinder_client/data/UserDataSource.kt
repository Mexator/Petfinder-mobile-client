package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.model.User
import io.reactivex.Completable
import io.reactivex.Single

interface UserDataSource {
    fun getUser(userCookie: String): Single<User>

    fun getFavorites(userCookie: String): Single<List<PetModel>>

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