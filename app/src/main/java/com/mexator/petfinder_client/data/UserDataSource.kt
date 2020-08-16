package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.model.User
import com.mexator.petfinder_client.data.remote.pojo.Favorite
import io.reactivex.Single

interface UserDataSource {
    fun getUser(userCookie: String): Single<User>

    fun getFavorites(userCookie: String): Single<List<PetModel>>
}