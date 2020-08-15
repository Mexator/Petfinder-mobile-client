package com.mexator.petfinder_client.mvvm.viewmodel.liked_pets

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.mvvm.viewstate.LikedPetsViewState
import com.mexator.petfinder_client.mvvm.viewstate.MainViewState
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent

class LikedPetViewModel : ViewModel(), KoinComponent {
    private val _viewState: BehaviorSubject<LikedPetsViewState> = BehaviorSubject.create()
    val viewState: Observable<LikedPetsViewState>
        get() = _viewState

    init {
        _viewState.onNext(
            LikedPetsViewState(
                emptyList()
            )
        )
    }

    fun loadNextPage() {
        TODO("Not yet implemented")
    }
}