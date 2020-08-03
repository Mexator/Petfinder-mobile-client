package com.mexator.petfinder_client.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

const val PAGINATION_OFFSET = 10
private const val fromPetTableQueryStr = "select * from ${PetEntity.TABLE_NAME} "
private const val deletePetTableQueryStr = "delete from ${PetEntity.TABLE_NAME} "

private const val whereTypeQueryStr = "type = :type "
private const val whereBreedQueryStr = "breeds like '%' || :breed || '%' "

private const val orderbyQueryStr = "order by orderArrived "
private const val limitQueryStr = "limit :offset, $PAGINATION_OFFSET"

@Dao
interface PetDao {

    @Query(
        fromPetTableQueryStr +
                "where " +
                whereBreedQueryStr + "and " +
                whereTypeQueryStr +
                orderbyQueryStr +
                limitQueryStr
    )
    fun getPets(type: String, breed: String, offset: Int): Single<List<PetEntity>>

    @Query(
        fromPetTableQueryStr +
                "where " +
                whereTypeQueryStr +
                orderbyQueryStr +
                limitQueryStr
    )
    fun getPetsByType(type: String, offset: Int): Single<List<PetEntity>>

    @Query(
        fromPetTableQueryStr +
                "where " +
                whereBreedQueryStr +
                orderbyQueryStr +
                limitQueryStr
    )
    fun getPetsByBreed(breed: String, offset: Int): Single<List<PetEntity>>

    @Query(
        fromPetTableQueryStr +
                limitQueryStr
    )
    fun getAllPets(offset: Int): Single<List<PetEntity>>

    @Query("select * from photos")
    fun getAllPhotos(): Single<List<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPet(pet: PetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPets(pets: List<PetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: PhotoEntity)

    @Query(deletePetTableQueryStr + "where " + whereTypeQueryStr)
    fun clearPetsTable(type: String)
}
