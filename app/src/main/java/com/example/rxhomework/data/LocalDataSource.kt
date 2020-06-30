package com.example.rxhomework.data

import com.example.rxhomework.ApplicationController
import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.PetDB
import com.example.rxhomework.storage.PetEntity
import com.example.rxhomework.storage.Type
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

object LocalDataSource : DataSource {
    private val db = PetDB.getDatabaseInstance(ApplicationController.context)
    override fun getPets(type: Type?, breed: Breed?): Single<List<Pet>> {
        val dao = db.petDao()
        return (if (type != null) {
            if (breed != null) dao.getPets(type, breed).map { it.map { it.toPet() } }
            else dao.getPetsByType(type).map { it.map { it.toPet() } }
        } else if (breed != null) dao.getPetsByBreed(breed).map { it.map { it.toPet() } }
        else dao.getAllPets().map { it.map { it.toPet() } }).subscribeOn(Schedulers.io())
    }

    fun savePets(it: Single<List<Pet>>): Unit {
        deletePets()
        it.subscribe({
            it?.forEach {
                db.petDao().insertPet(
                    PetEntity(
                        it.id,
                        it.age,
                        it.name,
                        it.description,
                        it.type,
                        it.breed,
                        ""
                    )
                )
            }
        }, {})
    }

    private fun deletePets(): Unit {
        db.petDao().clearPetsTable()
    }
}
