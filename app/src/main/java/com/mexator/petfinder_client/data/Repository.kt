package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.pojo.Pet
import io.reactivex.Single

interface Repository {
    fun getPets(animalType:String?=null, animalBreed:String?=null, page:Int?=1):Single<List<Pet>>

    fun areUserCredentialsValid(username:String, password:String):Single<Boolean>
}
