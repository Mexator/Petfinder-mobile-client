package com.mexator.petfinder_client.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.PetRepository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import com.mexator.petfinder_client.mvvm.viewstate.MainViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class PetSearchViewModel : ViewModel(), KoinComponent {
    val viewState: Observable<MainViewState> get() = _viewState

    var refreshNeeded: Boolean = true
        private set

    private var _viewState: BehaviorSubject<MainViewState> = BehaviorSubject.create()

    private val petRepository: PetRepository by inject()

    private val compositeDisposable = CompositeDisposable()

    // State variables. noMorePages: Last loaded page was empty. currentPage: Last non-empty page number
    private var noMorePages: Boolean = false
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

    fun reloadPetsList(type: String?, breed: String?) {
        _viewState.value?.let { state ->
            if (!state.updating) {
                _viewState.onNext(state.copy(updating = true))
                currentPage = 1
                petRepository.submitQuery(SearchParameters(type, breed))

                val job = petRepository.getPage(currentPage)
                    .subscribeOn(Schedulers.io())
                    .subscribe { value ->
                        receiveUpdate(value, type, breed)
                    }
                compositeDisposable.add(job)
            }
        }
    }

    fun loadNextPage() {
        _viewState.value?.let { state ->
            if (!state.updating && !noMorePages) {
                _viewState.onNext(state.copy(updating = true))
                val job = petRepository.getPage(++currentPage)
                    .subscribeOn(Schedulers.io())
                    .subscribe { value ->
                        receivePage(state, value)
                    }
                compositeDisposable.add(job)
            }
        }
    }

    private fun receiveUpdate(value: List<PetModel>, type: String?, breed: String?) {
        _viewState.onNext(
            MainViewState(
                updating = false,
                petList = value,
                requestType = type,
                requestBreed = breed
            )
        )
        refreshNeeded = value.isEmpty()
        noMorePages = value.isEmpty()
    }

    private fun receivePage(state: MainViewState, page: List<PetModel>) {
        _viewState.onNext(
            state.copy(
                updating = false,
                petList = state.petList + page
            )
        )
        noMorePages = page.isEmpty()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
