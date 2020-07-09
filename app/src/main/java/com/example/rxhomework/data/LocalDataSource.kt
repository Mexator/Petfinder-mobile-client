package com.example.rxhomework.data

import android.content.Context
import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.PetDB
import com.example.rxhomework.storage.PetEntity
import com.example.rxhomework.storage.Type
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

object LocalDataSource : DataSource, KoinComponent {
    private val appContext: Context by inject()
    private val db = PetDB.getDatabaseInstance(appContext)
    override fun getPets(animalType: Type?, animalBreed: Breed?, page: Int): Single<List<Pet>> {
        val dao = db.petDao()
        return (if (animalType != null) {
            if (animalBreed != null) dao.getPets(animalType, animalBreed)
                .map { it.map { it.toPet() } }
            else dao.getPetsByType(animalType).map { it.map { it.toPet() } }
        } else if (animalBreed != null) dao.getPetsByBreed(animalBreed)
            .map { it.map { it.toPet() } }
        else dao.getAllPets().map { it.map { it.toPet() } }).subscribeOn(Schedulers.io())
    }

    fun savePets(it: Single<List<Pet>>): Unit {
        deletePets()
        val job = it.map {
            it.forEach {
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
        }
    }

    private fun deletePets(): Unit {
        db.petDao().clearPetsTable()
    }
}
