package com.example.rxhomework.data

import com.example.rxhomework.data.pojo.PetEntity
import io.reactivex.Single

interface Repository {
    fun getPets(animalType:String?=null, animalBreed:String?=null):Single<List<PetEntity>>
}
