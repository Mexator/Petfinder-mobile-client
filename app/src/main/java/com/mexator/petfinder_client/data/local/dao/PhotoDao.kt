package com.mexator.petfinder_client.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mexator.petfinder_client.data.local.entity.PhotoEntity
import io.reactivex.Maybe

@Dao
interface PhotoDao {
    @Insert
    fun savePhoto(photoEntity: PhotoEntity)

    @Query("select * from ${PhotoEntity.TABLE_NAME} where petId = :petId")
    fun getPhotos(petId: Long): List<PhotoEntity>

    @Query("select * from ${PhotoEntity.TABLE_NAME} where petId = :petId limit 1")
    fun getPreview(petId: Long): Maybe<PhotoEntity>
}