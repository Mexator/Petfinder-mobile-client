package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.model.User
import io.reactivex.Single

interface UserDataRepository {

    /**
     * Check user login and password for validity. Save user cookie to preferences in case of success.
     * First lookup at local data source then at remote
     */
    fun login(username: String, password: String): Single<Boolean>

    /**
     * Get user model
     */
    fun getUser(): Single<User>
}