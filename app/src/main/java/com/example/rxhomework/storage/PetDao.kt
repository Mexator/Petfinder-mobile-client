package com.example.rxhomework.storage

import androidx.room.*
import com.example.rxhomework.data.pojo.Breed
import com.example.rxhomework.data.pojo.PetEntity
import com.example.rxhomework.data.pojo.Type
import io.reactivex.Single

@Dao
interface PetDao {
    @Query("select * from ${PetEntity.TABLE_NAME} where ${PetEntity.BREED} = :breed and ${PetEntity.TYPE} = :type")
    fun getPets(type: Type, breed: Breed): Single<List<PetEntity>>
    @Query("select * from ${PetEntity.TABLE_NAME} where ${PetEntity.TYPE} = :type")
    fun getPetsByType(type: Type): Single<List<PetEntity>>
    @Query("select * from ${PetEntity.TABLE_NAME} where ${PetEntity.BREED} = :breed")
    fun getPetsByBreed(breed: Breed): Single<List<PetEntity>>
    @Query("select * from ${PetEntity.TABLE_NAME}")
    fun getAllPets(): Single<List<PetEntity>>
    @Query("select * from ${PhotoEntity.TABLE_NAME}")
    fun getAllPhotos(): Single<List<PhotoEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPet(pet: PetEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: PhotoEntity)

    @Query("delete from ${PetEntity.TABLE_NAME}")
    fun clearPetsTable()
}
