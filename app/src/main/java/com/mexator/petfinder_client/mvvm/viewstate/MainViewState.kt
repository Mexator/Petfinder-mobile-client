package com.mexator.petfinder_client.mvvm.viewstate

import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.storage.Breed
import com.mexator.petfinder_client.storage.Type

data class MainViewState(
    val updating: Boolean,
    val petList: List<Pet>,
    val requestType: Type?,
    val requestBreed: Breed?
)