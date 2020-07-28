package com.mexator.petfinder_client.data.pojo

import android.os.Parcelable
import com.mexator.petfinder_client.data.model.PetModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PetResponse(
    override val id: Long,
    override val url: String,
    override val age: String?,
    override val gender: String,
    override val name: String,
    override val description: String?,
    override val type: String,
    override val breeds: Breed,
    val photos: List<PetPhotoResponse>?
) : Parcelable, PetModel() {
    override val source: StorageLocation
        get() = StorageLocation.REMOTE
}
