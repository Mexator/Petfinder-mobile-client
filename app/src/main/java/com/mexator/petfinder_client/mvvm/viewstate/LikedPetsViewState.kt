package com.mexator.petfinder_client.mvvm.viewstate

import com.mexator.petfinder_client.data.model.PetModel

data class LikedPetsViewState(
    val petList: List<PetModel>,
    val updating: Boolean
)