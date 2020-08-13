package com.mexator.petfinder_client.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.mvvm.viewstate.MainActivityViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainActivityViewModel : ViewModel(), KoinComponent {
    private val _viewState: BehaviorSubject<MainActivityViewState> = BehaviorSubject.create()
    val viewState: Observable<MainActivityViewState> get() = _viewState

    private val repository: UserDataRepository by inject()

    private var compositeDisposable = CompositeDisposable()

    init {
        _viewState.onNext(MainActivityViewState(null))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun fetchUser() {
        val job = repository
            .getUser()
            .subscribeOn(Schedulers.io())
            .subscribe { value ->
                val state = MainActivityViewState(value)
                _viewState.onNext(state)
            }
        compositeDisposable.add(job)
    }

    fun logout() {
        repository.logout()
    }
}