package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.pojo.User
import io.reactivex.Single

interface UserDataRepository {

    fun areUserCredentialsValid(username: String, password: String): Single<Boolean>

    fun getUser(): Single<User>
}