package com.mexator.petfinder_client.data.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PetPhoto(
    val small: String,
    val medium: String,
    val large: String,
    val full: String
):Parcelable