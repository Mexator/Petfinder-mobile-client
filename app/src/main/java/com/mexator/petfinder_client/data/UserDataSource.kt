package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.model.User
import io.reactivex.Single

interface UserDataSource {
    fun getUser(userCookie: String): Single<User>
}