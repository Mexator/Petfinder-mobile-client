package com.example.rxhomework.storage

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PetDao {
    @Query("select * from ${PetEntity.TABLE_NAME} where ${PetEntity.BREED} = :breed and ${PetEntity.TYPE} = :type")
    fun getPets(type: Type, breed: Breed)
}
