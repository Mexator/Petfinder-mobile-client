package com.mexator.petfinder_client.mvvm.viewmodel.liked_pets

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.mvvm.viewstate.MainViewState
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent

class LikedPetViewModel : ViewModel(), KoinComponent {
    private val _viewState: BehaviorSubject<MainViewState> = BehaviorSubject.create()
    val viewState: Observable<MainViewState>
        get() = _viewState

    init {
        _viewState.onNext(
            MainViewState(
                updating = false,
                petList = emptyList(),
                requestBreed = null,
                requestType = null,
                error = null
            )
        )
    }

    fun loadNextPage() {
        TODO("Not yet implemented")
    }

    fun reloadPetsList(type: String?, breed: String?) {
        TODO("Not yet implemented")
    }

    val refreshNeeded: Boolean
        get() = TODO("Not yet implemented")
}