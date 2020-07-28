package com.mexator.petfinder_client.storage

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.pojo.Breed
import com.mexator.petfinder_client.storage.converter.BreedConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(BreedConverter::class)
@Entity(tableName = PetEntity.TABLE_NAME)
data class PetEntity(
    @PrimaryKey
    override val id: Long,
    override val url: String,
    override val age: String?,
    override val gender: String,
    override val name: String,
    override val description: String?,
    override val type: String,
    override val breeds: Breed
) : PetModel() {
    override val source: StorageLocation
        get() = StorageLocation.LOCAL

    companion object {
        const val TABLE_NAME = "pets"
    }
}
