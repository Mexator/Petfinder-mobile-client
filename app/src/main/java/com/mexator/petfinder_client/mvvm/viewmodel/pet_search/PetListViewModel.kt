package com.mexator.petfinder_client.mvvm.viewmodel.pet_search

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.mvvm.viewstate.MainViewState
import io.reactivex.Observable

abstract class PetListViewModel: ViewModel() {
    abstract fun loadNextPage()
    abstract fun reloadPetsList(type: String?, breed: String?)
    abstract val refreshNeeded: Boolean
    abstract val viewState: Observable<MainViewState>
}