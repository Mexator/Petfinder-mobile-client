package com.mexator.petfinder_client.data.actual

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.mexator.petfinder_client.data.DataSource
import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.extensions.getTag
import com.mexator.petfinder_client.storage.PetDB
import com.mexator.petfinder_client.storage.PetEntity
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

object LocalDataSource : DataSource<PetEntity>, KoinComponent {
    private val appContext: Context by inject()
    private val db = PetDB.getDatabaseInstance(appContext)

    override fun getPets(animalType: String?, animalBreed: String?, page: Int): Single<List<Pet>> =
        with(db.petDao()) {
            when {
                animalType == null && animalBreed == null -> getAllPets()
                animalType == null && animalBreed != null -> getPetsByBreed(animalBreed)
                animalType != null && animalBreed == null -> getPetsByType(animalType)
                else -> getPets(animalType!!, animalBreed!!)
            }
        }.map { list -> list.map { petEntity -> petEntity.toPet() } }


    @SuppressLint("CheckResult") // Because result can be delivered unless app is destroyed
    public fun savePets(content: List<Pet>) {
        deletePets()
        val entities = content.map {
            with(it) {
                PetEntity(
                    id,
                    url,
                    age,
                    gender,
                    name,
                    description,
                    type,
                    breeds.primary
                )
            }
        }
        db.petDao().insertPets(entities)
    }

    override fun getPetPhotos(
        pet: PetEntity,
        size: DataSource.PhotoSize
    ): Single<List<Drawable>> {
        TODO("Not yet implemented")
    }

    private fun deletePets() {
        db.petDao().clearPetsTable()
    }
}
