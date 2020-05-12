package com.example.rxhomework.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = PetEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("petId"),
    onDelete = CASCADE)]
)
data class PhotoEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val small: String,
    val medium: String
)
