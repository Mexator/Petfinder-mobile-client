package com.mexator.petfinder_client.mvvm.viewstate

import com.mexator.petfinder_client.ui.petlist.PetHolder

/**
 * [PetSearchViewState] represents state of Pet Search screen
 */
data class PetSearchViewState(
    val updating: Boolean,
    val petList: List<PetHolder>,
    val requestType: String?,
    val requestBreed: String?,
    val error: String?
)