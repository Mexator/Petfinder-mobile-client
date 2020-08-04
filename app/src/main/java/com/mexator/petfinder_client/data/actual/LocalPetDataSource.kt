package com.mexator.petfinder_client.data.actual

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import androidx.sqlite.db.SimpleSQLiteQuery
import com.mexator.petfinder_client.data.PetDataSource
import com.mexator.petfinder_client.data.pojo.PetResponse
import com.mexator.petfinder_client.data.pojo.SearchParameters
import com.mexator.petfinder_client.storage.*
import com.mexator.petfinder_client.utils.WhereBuilder
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.math.BigInteger
import java.util.*

object LocalPetDataSource : PetDataSource<PetEntity>, KoinComponent {
    private val appContext: Context by inject()
    private val db = PetDB.getDatabaseInstance(appContext)

    override fun getPets(parameters: SearchParameters, page: Int): Single<List<PetEntity>> {
        val builder = WhereBuilder("select * from ${PetEntity.TABLE_NAME} ")

        parameters.apply {
            animalType?.let { builder.addAndCondition("type = \"$it\" ") }
            animalBreed?.let { builder.addAndCondition("breeds like '%' || \"$it\" || '%' ") }
        }

        val query = SimpleSQLiteQuery(
            builder.query +
                    "order by orderArrived " +
                    "limit ${toOffset(page)}, $PAGINATION_OFFSET"
        )
        return db.petDao().getPetsRawQuery(query)
    }

    private var count = 0

    /**
     * Save pets to DB
     */
    fun savePets(content: List<PetResponse>, clear: Boolean) {
        if (clear and content.isNotEmpty()) {
            deletePets(content[0].type)
            count = 0
        }
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
                    breeds,
                    count++
                )
            }
        }
        db.petDao().insertPets(entities)
    }

    /**
     * Save a list of photos to filesystem, create photoEntity for each of them,
     * link them to the corresponding pet.
     */
    fun savePetPhotos(photos: List<Drawable>, petId: Long) {

        for (photo in photos) {
            val path = StorageManager.writeBitmapTo(randomName(), photo.toBitmap())
            db.photoDao().savePhoto(PhotoEntity(path, petId))
        }
    }

    override fun getPetPhotos(
        pet: PetEntity,
        size: PetDataSource.PhotoSize
    ): Single<List<Drawable>> {
        return db.photoDao()
            .getPhotos(pet.id)
            .map { list -> list.map { element -> Drawable.createFromPath(element.fileName)!! } }
    }

    private fun randomName(nameLen: Int = 32): String {
        var name = BigInteger(nameLen, Random())
            .toString()
        if (name.length < nameLen) {
            val zeros = "0".repeat(nameLen - name.length)
            name = zeros + name
        }
        return name
    }

    private fun deletePets(type: String) {
        db.petDao().clearPetsTable(type)
    }

    private fun toOffset(page: Int?, itemsOnPage: Int = PAGINATION_OFFSET): Int =
        ((page ?: 1) - 1) * itemsOnPage
}
