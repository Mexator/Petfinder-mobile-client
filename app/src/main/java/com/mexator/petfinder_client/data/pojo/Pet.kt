package com.mexator.petfinder_client.data.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pet(
    val id: Long,
    val url: String,
    val age: String?,
    val name: String,
    val description: String?,
    val type: String,
    val breed: String?,
    val photos: List<PetPhoto>?
) : Parcelable