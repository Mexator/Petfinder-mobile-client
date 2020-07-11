package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.pojo.Pet
import io.reactivex.Single

interface DataSource {
    fun getPets(animalType:String?=null, animalBreed:String?=null, page:Int=1):Single<List<Pet>>
}
