package com.mexator.petfinder_client.data.model

import android.os.Parcelable
import com.mexator.petfinder_client.data.remote.pojo.Breed

abstract class PetModel: Parcelable {
    abstract val id: Long
    abstract val url: String
    abstract val age: String?
    abstract val gender: String
    abstract val name: String
    abstract val description: String?
    abstract val type: String
    abstract val breeds: Breed
    abstract val source: StorageLocation

    enum class StorageLocation { LOCAL, REMOTE }
}
