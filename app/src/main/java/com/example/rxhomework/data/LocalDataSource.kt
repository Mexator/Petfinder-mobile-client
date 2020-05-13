package com.example.rxhomework.data

import com.example.rxhomework.ApplicationController
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.PetDB
import com.example.rxhomework.storage.PetEntity
import com.example.rxhomework.storage.Type
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class LocalDataSource : DataSource {
    private val db = PetDB.getDatabaseInstance(ApplicationController.context)
    override fun getPets(type: Type?, breed: Breed?): Single<List<PetEntity>> {
        val dao = db.petDao()
        return (if (type != null) {
            if (breed != null) dao.getPets(type, breed)
            else dao.getPetsByType(type)
        } else if (breed != null) dao.getPetsByBreed(breed)
        else dao.getAllPets()).subscribeOn(Schedulers.io())
    }
}
