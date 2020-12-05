package com.mexator.petfinder_client.mvvm.viewstate

import com.mexator.petfinder_client.ui.petlist.PetHolder

/**
 * [MainViewState] represents state of Pet Search screen
 */
data class MainViewState(
    val updating: Boolean,
    val petList: List<PetHolder>,
    val requestType: String?,
    val requestBreed: String?,
    val error: String?
)