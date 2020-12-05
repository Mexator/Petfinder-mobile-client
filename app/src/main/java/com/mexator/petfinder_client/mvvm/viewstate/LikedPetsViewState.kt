package com.mexator.petfinder_client.mvvm.viewstate

import com.mexator.petfinder_client.ui.petlist.PetHolder

/**
 * [LikedPetsViewState] represents state of screen with liked pets
 */
data class LikedPetsViewState(
    val petList: List<PetHolder>,
    val updating: Boolean
)