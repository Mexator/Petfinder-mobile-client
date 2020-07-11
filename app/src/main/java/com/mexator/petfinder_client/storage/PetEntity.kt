package com.mexator.petfinder_client.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mexator.petfinder_client.data.pojo.Pet

typealias Type = String
typealias Age = String
typealias Name = String
typealias Description = String
typealias Breed = String

@Entity(tableName = PetEntity.TABLE_NAME)
/**
 * Represents both POJO Pet class for network request and data stored in the local database
 */
data class PetEntity(
    @PrimaryKey
    val id: Long,
    val age: Age?,
    val name: Name,
    val description: Description?,
    @ColumnInfo(name = TYPE)
    val type: Type,
    @ColumnInfo(name = BREED)
    val breed: Breed?,
    val previewPicPath: String?
) {
    companion object {
        const val TABLE_NAME = "pets"
        const val TYPE = "type"
        const val BREED = "breed"
    }
    fun toPet(): Pet {
        return Pet(id, age, name, description, type, breed, null)
    }
}
