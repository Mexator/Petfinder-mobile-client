package com.mexator.petfinder_client.data.local.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.mexator.petfinder_client.data.local.entity.PetEntity
import com.mexator.petfinder_client.data.local.entity.PhotoEntity
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

    @Query("select * from pets where id == :id")
    fun getPet(id:Long): Single<PetEntity>
}
