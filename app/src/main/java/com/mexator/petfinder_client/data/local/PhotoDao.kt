package com.mexator.petfinder_client.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface PhotoDao {
    @Insert
    fun savePhoto(photoEntity: PhotoEntity)

    @Query("select * from ${PhotoEntity.TABLE_NAME} where petId = :petId")
    fun getPhotos(petId: Long): Single<List<PhotoEntity>>
}