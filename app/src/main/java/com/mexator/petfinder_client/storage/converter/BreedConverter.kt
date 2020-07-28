package com.mexator.petfinder_client.storage.converter

import androidx.room.TypeConverter
import com.mexator.petfinder_client.data.pojo.Breed

class BreedConverter {
    @TypeConverter
    fun fromBreed(breed: Breed): String {
        with(breed) {
            return "$primary, $secondary, $mixed, $unknown"
        }
    }

    @TypeConverter
    fun toBreed(string: String): Breed {
        with(string.split(",")) {
            return Breed(get(0), get(1), get(2).toBoolean(), get(3).toBoolean())
        }
    }
}