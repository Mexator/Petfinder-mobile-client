package com.mexator.petfinder_client.mvvm.viewmodel.pet_search

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.mvvm.viewstate.MainViewState
import io.reactivex.Observable

/**
 * ViewModel for view that shows list of pets
 */
abstract class PetListViewModel : ViewModel() {
    /**
     * Exposed view state
     */
    abstract val viewState: Observable<MainViewState>

    /**
     * True, if the viewModel contains no items at all or item list needs to be refreshed
     */
    abstract val refreshNeeded: Boolean

    /**
     * Get new items for the list exposed with viewState
     */
    abstract fun loadNextPage()

    /**
     * Invalidate current list and reload
     */
    abstract fun reloadPetsList(type: String?, breed: String?)
}