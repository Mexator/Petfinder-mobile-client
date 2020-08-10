package com.mexator.petfinder_client.mvvm.viewmodel.pet_detail

import android.graphics.drawable.Drawable

data class PhotoWrapper(
    var photo: Drawable?,
    val id: Long,
    val isPlaceholder: Boolean
)