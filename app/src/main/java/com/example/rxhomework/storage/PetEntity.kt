package com.example.rxhomework.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

typealias Type = String
typealias Age = String
typealias Name = String
typealias Description = String
typealias Breed = String

@Entity(tableName = PetEntity.TABLE_NAME)
data class PetEntity(
    @PrimaryKey
    val id: Long,
    val age: Age?,
    val name: Name,
    val description: Description,
    @ColumnInfo(name = TYPE)
    val type: Type,
    @ColumnInfo(name = BREED)
    val breed: Breed?
) {
    companion object {
        const val TABLE_NAME = "pets"
        const val TYPE = "type"
        const val BREED = "breed"
    }
}
