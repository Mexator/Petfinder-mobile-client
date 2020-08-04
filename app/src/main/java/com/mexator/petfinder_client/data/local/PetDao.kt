package com.mexator.petfinder_client.data.local

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import io.reactivex.Single

const val PAGINATION_OFFSET = 10

@Dao
interface PetDao {
    @RawQuery
    fun getPetsRawQuery(query: SupportSQLiteQuery): Single<List<PetEntity>>


    @Query("select * from photos")
    fun getAllPhotos(): Single<List<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPet(pet: PetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPets(pets: List<PetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: PhotoEntity)

    @Query("delete from ${PetEntity.TABLE_NAME} where type = :type ")
    fun clearPetsTable(type: String)
}
