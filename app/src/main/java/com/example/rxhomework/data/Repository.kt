package com.example.rxhomework.data

import com.example.rxhomework.data.pojo.Pet
import io.reactivex.Single

interface Repository {
    fun getPets(animalType:String?=null, animalBreed:String?=null, page:Int?=1):Single<List<Pet>>
}
