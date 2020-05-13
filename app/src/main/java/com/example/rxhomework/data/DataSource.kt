package com.example.rxhomework.data

import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.PetEntity
import com.example.rxhomework.storage.Type
import io.reactivex.Single

interface DataSource {

    fun getPets(animalType:String?=null, animalBreed:String?=null):Single<List<PetEntity>>
}
