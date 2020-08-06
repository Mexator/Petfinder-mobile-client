package com.mexator.petfinder_client.mvvm.viewstate

import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.mvvm.viewmodel.pet_detail.PhotoWrapper

data class PetDetailViewState(
    val photos: List<PhotoWrapper>,
    val photoCount: Int,
    val petData: PetModel
)