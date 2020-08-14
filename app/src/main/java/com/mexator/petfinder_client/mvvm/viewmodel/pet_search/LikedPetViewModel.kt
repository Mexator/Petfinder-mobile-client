package com.mexator.petfinder_client.mvvm.viewmodel.pet_search

import com.mexator.petfinder_client.mvvm.viewstate.MainViewState
import io.reactivex.Observable
import org.koin.core.KoinComponent

class LikedPetViewModel: PetListViewModel(), KoinComponent {
    override fun loadNextPage() {
        TODO("Not yet implemented")
    }

    override fun reloadPetsList(type: String?, breed: String?) {
        TODO("Not yet implemented")
    }

    override val refreshNeeded: Boolean
        get() = TODO("Not yet implemented")
    override val viewState: Observable<MainViewState>
        get() = TODO("Not yet implemented")
}