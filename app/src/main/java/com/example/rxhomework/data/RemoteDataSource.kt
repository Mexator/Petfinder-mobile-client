package com.example.rxhomework.data

import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.PetEntity
import com.example.rxhomework.storage.Type
import io.reactivex.Single

class RemoteDataSource : DataSource {
    override fun getPets(type: Type?, breed: Breed?): Single<List<PetEntity>> {
        val pets = ArrayList<PetEntity>()
        val pet = PetEntity(0,"remotePet", "", "", "null", null, null)
        pets.add(pet)
        return Single.just(pets)
    }
}
