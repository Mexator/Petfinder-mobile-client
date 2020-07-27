package com.mexator.petfinder_client.data.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Breed (
    val primary:String?,
    val secondary:String?,
    val mixed:Boolean,
    val unknown:Boolean
): Parcelable