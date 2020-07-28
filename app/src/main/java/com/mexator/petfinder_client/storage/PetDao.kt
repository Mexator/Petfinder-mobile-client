package com.mexator.petfinder_client.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface PetDao {
    @Query("select * from ${PetEntity.TABLE_NAME} where breeds like '%' || :breed || '%' and type = :type")
    fun getPets(type: String, breed: String): Single<List<PetEntity>>

    @Query("select * from ${PetEntity.TABLE_NAME} where type = :type")
    fun getPetsByType(type: String): Single<List<PetEntity>>

    @Query("select * from ${PetEntity.TABLE_NAME} where breeds like '%' || :breed || '%'")
    fun getPetsByBreed(breed: String): Single<List<PetEntity>>

    @Query("select * from ${PetEntity.TABLE_NAME}")
    fun getAllPets(): Single<List<PetEntity>>

    @Query("select * from photos")
    fun getAllPhotos(): Single<List<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPet(pet: PetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPets(pets: List<PetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: PhotoEntity)

    @Query("delete from ${PetEntity.TABLE_NAME}")
    fun clearPetsTable()
}
