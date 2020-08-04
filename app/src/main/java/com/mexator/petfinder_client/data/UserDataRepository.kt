package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.remote.pojo.User
import io.reactivex.Single

interface UserDataRepository {

    /**
     * Check user login and password for validity
     */
    fun areUserCredentialsValid(username: String, password: String): Single<Boolean>

    /**
     * Get user model
     */
    fun getUser(): Single<User>
}