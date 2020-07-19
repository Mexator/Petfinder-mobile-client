package com.mexator.petfinder_client.data

import android.content.Context
import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.storage.Breed
import com.mexator.petfinder_client.storage.PetDB
import com.mexator.petfinder_client.storage.PetEntity
import com.mexator.petfinder_client.storage.Type
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

    fun savePets(content: Single<List<Pet>>) {
        deletePets()
        val job = content.doOnEvent{ list: List<Pet>, _ ->
            list.forEach {
                db.petDao().insertPet(
                    PetEntity(
                        it.id,
                        it.age,
                        it.name,
                        it.description,
                        it.type,
                        it.breeds.primary,
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
