package com.mexator.petfinder_client.storage

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.pojo.Breed
import com.mexator.petfinder_client.data.pojo.Pet

@Entity(tableName = PetEntity.TABLE_NAME)
data class PetEntity(
    @PrimaryKey
    val id: Long,
    val url: String,
    val age: String?,
    val gender: String,
    val name: String,
    val description: String?,
    val type: String,
    val breed: String?
) : PetModel() {
    override val source: StorageLocation
        get() = StorageLocation.LOCAL

    companion object {
        const val TABLE_NAME = "pets"
    }

    fun toPet(): Pet {
        return Pet(
            id,
            url,
            age,
            gender,
            name,
            description,
            type,
            if (breed != null)
                Breed(breed, null, mixed = false, unknown = false)
            else
                Breed(null, null, mixed = false, unknown = true),
            null
        )
    }
}
