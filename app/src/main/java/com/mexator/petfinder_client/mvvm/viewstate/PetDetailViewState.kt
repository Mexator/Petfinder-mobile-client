package com.mexator.petfinder_client.mvvm.viewstate

import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.model.PetModel

data class PetDetailViewState(
    val photos: List<Drawable?>,
    val photoCount: Int,
    val petData: PetModel
)