package com.mexator.petfinder_client.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
//    foreignKeys = [ForeignKey(
//        onDelete = CASCADE,
//        entity = PetEntity::class,
//        parentColumns = ["id"],
//        childColumns = ["id"]
//    )]
)
data class FavoriteEntity(
    @PrimaryKey
    val id: Long
)