package com.mexator.petfinder_client.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.Repository
import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.mvvm.viewstate.MainViewState
import com.mexator.petfinder_client.storage.Breed
import com.mexator.petfinder_client.storage.Type
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel : ViewModel(), KoinComponent {
    var listNotEmpty = false
    private val repository: Repository by inject()

    private val compositeDisposable = CompositeDisposable()

    private var _viewState: BehaviorSubject<MainViewState> = BehaviorSubject.create()
    val viewState: Observable<MainViewState> get() = _viewState

    private var currentPage: Int = 1

    init {
        _viewState.onNext(
            MainViewState(
                false,
                emptyList(),
                null,
                null
            )
        )
    }

    fun updatePetsList(type: Type?, breed: Breed?) {
        _viewState.value?.let { state ->
            if (!state.updating) {
                _viewState.onNext(state.copy(updating = true))

                val job = repository.getPets(type, breed)
                    .subscribe { value ->
                        receiveUpdate(value, type, breed)
                    }
                compositeDisposable.add(job)
            }
        }
    }

    fun loadNextPage() {
        _viewState.value?.let { state ->
            if (!state.updating) {
                _viewState.onNext(state.copy(updating = true))
                val job = repository.getPets(state.requestType, state.requestBreed, ++currentPage)
                    .subscribe { value ->
                        receivePage(state, value)
                    }
                compositeDisposable.add(job)
            }
        }
    }

    private fun receiveUpdate(value: List<Pet>, type: Type?, breed: Breed?) {
        listNotEmpty = value.isNotEmpty()
        currentPage = 1
        _viewState.onNext(
            MainViewState(
                updating = false,
                petList = value,
                requestType = type,
                requestBreed = breed
            )
        )
    }

    private fun receivePage(state: MainViewState, page: List<Pet>) {
        _viewState.onNext(
            state.copy(
                updating = false,
                petList = state.petList + page
            )
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
