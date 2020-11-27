package com.mexator.petfinder_client.data.actual

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.sqlite.db.SimpleSQLiteQuery
import com.mexator.petfinder_client.data.PetDataSource
import com.mexator.petfinder_client.data.UserDataSource
import com.mexator.petfinder_client.data.local.PetDB
import com.mexator.petfinder_client.data.local.dao.PAGINATION_OFFSET
import com.mexator.petfinder_client.data.local.entity.FavoriteEntity
import com.mexator.petfinder_client.data.local.entity.PetEntity
import com.mexator.petfinder_client.data.local.entity.PhotoEntity
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.model.User
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import com.mexator.petfinder_client.extensions.getTag
import com.mexator.petfinder_client.storage.StorageManager
import com.mexator.petfinder_client.utils.WhereBuilder
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.math.BigInteger
import java.util.*

/**
 * [LocalDataSource] is an object that wraps local database to make it more
 * convenient to use. Implements [UserDataSource] and [PetDataSource].
 */
object LocalDataSource : PetDataSource<PetEntity>, UserDataSource, KoinComponent {
    private val appContext: Context by inject()
    private val db = PetDB.getDatabaseInstance(appContext)

    override fun getPets(
        parameters: SearchParameters,
        page: Int
    ): Single<List<PetEntity>> {
        val builder = WhereBuilder(
            "select * from ${PetEntity.TABLE_NAME} "
        )

        parameters.apply {
            animalType?.let {
                builder.addAndCondition(
                    "type = \"$it\" "
                )
            }
            animalBreed?.let {
                builder.addAndCondition(
                    "breeds like '%' || \"$it\" || '%' "
                )
            }
        }

        val query = SimpleSQLiteQuery(
            builder.query +
                    "order by orderArrived " +
                    "limit ${toOffset(page)}, $PAGINATION_OFFSET"
        )
        return db.petDao().getPetsRawQuery(query)
    }

    override fun getPetPhotos(
        pet: PetEntity, size: PetDataSource.PhotoSize
    ): List<Single<Drawable>> =
        db.photoDao()
            .getPhotos(pet.id)
            .subscribeOn(Schedulers.io())
            .blockingGet()
            .map {
                Single.defer {
                    Single.just(Drawable.createFromPath(it.fileName)!!)
                }
            }


    override fun getPetPreview(pet: PetEntity): Maybe<Drawable> =
        db.photoDao().getPreview(pet.id)
            .map { Drawable.createFromPath(it.fileName) }

    override fun getUser(userCookie: String): Single<User> =
        db.userDao().getUser()

    override fun getPet(id: Long): Maybe<PetEntity> =
        db.petDao()
            .getPet(id)
            .toMaybe()
            .onErrorComplete()

    override fun getFavorites(userCookie: String): Single<List<PetModel>> =
        db.userDao()
            .getFavorites()
            .map { list -> list.map { it as PetModel } }

    override fun getFavoritesIDs(userCookie: String): Single<List<Long>> {
        return db.userDao()
            .getFavoriteIDs()
    }

    override fun addFavorite(userCookie: String, pet: PetModel) =
        db.userDao()
            .saveFavorites(listOf(FavoriteEntity(pet.id)))

    override fun removeFavorite(userCookie: String, pet: PetModel): Completable =
        db.userDao()
            .removeFavorite(FavoriteEntity(pet.id))

    fun updateFavorites(favorites: List<Long>) {
        db.userDao()
            .updateFavorites(favorites.map { FavoriteEntity(it) })
    }

    fun saveUser(user: User) {
        with(db.userDao()) {
            deleteCurrentUser()
            saveUser(user)
        }
    }

    /**
     * Delete all user-related data from database
     */
    fun deleteUser() {
        db.userDao().deleteCurrentUser()
            .subscribeOn(Schedulers.io())
            .subscribe()
        db.userDao().clearFavorites()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    // Used to index pets by order they arrived
    private var count = 0

    /**
     * Save pets to DB
     */
    fun savePets(content: List<PetModel>, clear: Boolean) {
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
     * Save a photo to filesystem, create photoEntity for each of them,
     * link them to the corresponding pet.
     */
    fun savePetPhoto(photo: Drawable, petId: Long) {
        val path = StorageManager.writeBitmapTo(randomName(), photo.toBitmap())
        db.photoDao().savePhoto(PhotoEntity(path, petId))
    }

    /**
     * Generate random filename for am image to be stored on disk
     */
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

    /**
     * Convert page number to offset for SQL query
     */
    private fun toOffset(page: Int?, itemsOnPage: Int = PAGINATION_OFFSET): Int =
        ((page ?: 1) - 1) * itemsOnPage

}
