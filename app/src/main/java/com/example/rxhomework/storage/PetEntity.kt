package com.example.rxhomework.storage

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class PetEntity (
    @PrimaryKey
    val id: Long,
    val age: String?,
    val name: String,
    val description: String
)
