package com.mexator.petfinder_client.mvvm.viewmodel.liked_pets

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.mvvm.viewstate.LikedPetsViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class LikedPetViewModel : ViewModel(), KoinComponent {
    private val _viewState: BehaviorSubject<LikedPetsViewState> = BehaviorSubject.create()
    val viewState: Observable<LikedPetsViewState>
        get() = _viewState

    private val repository: UserDataRepository by inject()
    private val compositeDisposable = CompositeDisposable()

    init {
        _viewState.onNext(
            LikedPetsViewState(
                emptyList()
            )
        )
    }

    fun loadNextPage() {
        val job = repository.getFavorites()
            .subscribeOn(Schedulers.io())
            .subscribe { value -> _viewState.onNext(LikedPetsViewState(value)) }
        compositeDisposable.add(job)
    }
}