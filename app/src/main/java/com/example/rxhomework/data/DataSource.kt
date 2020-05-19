package com.example.rxhomework.data

import com.example.rxhomework.storage.PetEntity
import io.reactivex.Single

interface DataSource {
    fun getPets(animalType:String?=null, animalBreed:String?=null):Single<List<PetEntity>>
}
