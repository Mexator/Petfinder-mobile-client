package com.mexator.petfinder_client.data

import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.pojo.User
import io.reactivex.Single

interface Repository {
    fun getPets(
        animalType: String? = null,
        animalBreed: String? = null,
        page: Int? = 1
    ): Single<List<PetModel>>

    fun getPetPhotos(
        pet: PetModel,
        size: DataSource.PhotoSize
    ): Single<List<Drawable>>

    fun areUserCredentialsValid(username: String, password: String): Single<Boolean>

    fun getUser(): Single<User>
}
