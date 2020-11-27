package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.model.User
import io.reactivex.Single

/**
 * [UserDataRepository] is a repository that provides any user-related data and
 * used to perform authentication.
 */
interface UserDataRepository {

    /**
     * Check user login and password for validity
     */
    fun login(username: String, password: String): Single<Boolean>

    /**
     * Delete all user data from the device
     */
    fun logout()

    /**
     * Get user model instance associated with current user
     */
    fun getUser(): Single<User>

    /**
     * Set user cookie. Used by StartActivity to load cookie from file
     * @TODO Remove method and change startup behavior to fetch cookie from server
     */
    fun setCookie(userCookie: String)

    /**
     * Get list of IDs of favorite pets. Usually this function is used when it
     * is needed to show correct state of "like" checkbox.
     */
    fun getFavoritesIDs() : Single<List<Long>>

    /**
     * Get list of favorite pets
     */
    fun getFavorites(): Single<List<PetModel>>

    /**
     * Mark pet as favorite
     */
    fun like(pet:PetModel)

    /**
     * Remove pet from favourites
     */
    fun unLike(pet: PetModel)
}