package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.model.User
import io.reactivex.Completable
import io.reactivex.Single

interface UserDataRepository {

    /**
     * Check user login and password for validity. Save user cookie to preferences in case of success.
     * First lookup at local data source then at remote
     */
    fun login(username: String, password: String): Single<Boolean>

    /**
     * Delete all user data from device
     */
    fun logout()

    /**
     * Get user model
     */
    fun getUser(): Single<User>

    /**
     * Set user cookie
     */
    fun setCookie(userCookie: String)

    /**
     * Get list of IDs of favorite pets
     */
    fun getFavoritesIDs() : Single<List<Long>>

    /**
     * Get list of favorite pets
     */
    fun getFavorites(): Single<List<PetModel>>

    /**
     * Mark pet as favourite
     */
    fun Like(pet:PetModel)
    /**
     * Remove pet from favourites
     */
    fun UnLike(pet: PetModel): Unit
}