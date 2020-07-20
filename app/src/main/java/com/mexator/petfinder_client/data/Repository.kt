package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.data.pojo.User
import io.reactivex.Single

interface Repository {
    fun getPets(
        animalType: String? = null,
        animalBreed: String? = null,
        page: Int? = 1
    ): Single<List<Pet>>

    fun areUserCredentialsValid(username: String, password: String): Single<Boolean>

    fun getUser(): Single<User>
}
