package com.mexator.petfinder_client.data.pojo

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.WriteWith

@Parcelize
data class Pet(
    val id: Long,
    val age: String?,
    val name: String,
    val description: String?,
    val type: String,
    val breed: String?,
    val photos: List<PetPhoto>?
) : Parcelable