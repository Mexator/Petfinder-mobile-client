package com.mexator.petfinder_client.data.actual

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.DataSource
import com.mexator.petfinder_client.data.pojo.PetResponse
import com.mexator.petfinder_client.storage.PetDB
import com.mexator.petfinder_client.storage.PetEntity
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

object LocalDataSource : DataSource<PetEntity>, KoinComponent {
    private val appContext: Context by inject()
    private val db = PetDB.getDatabaseInstance(appContext)

    override fun getPets(animalType: String?, animalBreed: String?, page: Int): Single<List<PetEntity>> =
        with(db.petDao()) {
            when {
                animalType == null && animalBreed == null -> getAllPets()
                animalType == null && animalBreed != null -> getPetsByBreed(animalBreed)
                animalType != null && animalBreed == null -> getPetsByType(animalType)
                else -> getPets(animalType!!, animalBreed!!)
            }
        }

    fun savePets(content: List<PetResponse>) {
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
                    breeds
                )
            }
        }
        db.petDao().insertPets(entities)
    }

    override fun getPetPhotos(
        pet: PetEntity,
        size: DataSource.PhotoSize
    ): Single<List<Drawable>> {
        return Single.just(listOf())
    }

    private fun deletePets() {
        db.petDao().clearPetsTable()
    }
}
