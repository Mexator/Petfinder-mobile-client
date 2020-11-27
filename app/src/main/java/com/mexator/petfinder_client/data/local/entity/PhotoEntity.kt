package com.mexator.petfinder_client.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = PhotoEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = PetEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("petId"),
        onDelete = CASCADE
    )]
)
data class PhotoEntity(
    @PrimaryKey
    val fileName: String,
    val petId: Long
) {
    companion object {
        const val TABLE_NAME = "photos"
    }
}
