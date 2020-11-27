package com.mexator.petfinder_client.mvvm.viewstate

import com.mexator.petfinder_client.data.model.PetModel

data class MainViewState(
    val updating: Boolean,
    val petList: List<PetModel>,
    val requestType: String?,
    val requestBreed: String?,
    val error: String?
)