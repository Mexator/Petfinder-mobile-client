package com.mexator.petfinder_client.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

const val PAGINATION_OFFSET = 10
private const val fromPetTableQueryStr = "select * from ${PetEntity.TABLE_NAME} "
private const val orderbyQueryStr = "order by orderArrived "
private const val limitQueryStr = "limit :offset, $PAGINATION_OFFSET"

private const val whereBothQueryStr = "where breeds like '%' || :breed || '%' and type = :type "
private const val whereTypeQueryStr = "where type = :type "
private const val whereBreedQueryStr = "where breeds like '%' || :breed || '%' "

@Dao
interface PetDao {

    @Query(
        fromPetTableQueryStr +
                whereBothQueryStr +
                orderbyQueryStr +
                limitQueryStr
    )
    fun getPets(type: String, breed: String, offset: Int): Single<List<PetEntity>>

    @Query(
        fromPetTableQueryStr +
                whereTypeQueryStr +
                orderbyQueryStr +
                limitQueryStr
    )
    fun getPetsByType(type: String, offset: Int): Single<List<PetEntity>>

    @Query(
        fromPetTableQueryStr +
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

    @Query("delete from ${PetEntity.TABLE_NAME}")
    fun clearPetsTable()
}
