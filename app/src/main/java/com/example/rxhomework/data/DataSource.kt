package com.example.rxhomework.data

import com.example.rxhomework.data.pojo.Pet
import io.reactivex.Single

interface DataSource {
    fun getPets(animalType:String?=null, animalBreed:String?=null, page:Int=1):Single<List<Pet>>
}
