package com.mexator.petfinder_client.mvvm.viewstate

import com.mexator.petfinder_client.data.pojo.Pet

data class MainViewState(
    val updating: Boolean,
    val petList: List<Pet>,
    val requestType: String?,
    val requestBreed: String?
)