package com.mexator.petfinder_client.mvvm.viewmodel.pet_search

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.PetRepository
import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import com.mexator.petfinder_client.extensions.getTag
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
    private val userDataRepository: UserDataRepository by inject()

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
                null,
                null
            )
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun reloadPetsList(type: String?, breed: String?) {
        _viewState.value?.let { state ->
            if (!state.updating) {
                _viewState.onNext(state.copy(updating = true, petList = emptyList()))
                currentPage = 1
                petRepository.submitQuery(SearchParameters(type, breed))

                requestPage(
                    { value -> receiveUpdate(value, type, breed) },
                    { error -> receivePageError(state, error.message ?: "Unknown error") }
                )
            }
        }
    }

    fun loadNextPage() {
        _viewState.value?.let { state ->
            if (!state.updating && !noMorePages) {
                _viewState.onNext(state.copy(updating = true))
                requestPage(
                    { value -> receivePage(state, value) },
                    { error -> receivePageError(state, error.message ?: "Unknown error") })
            }
        }
    }

    fun addToFavorites(pet: PetModel) {
        userDataRepository.Like(pet)
    }

    fun removeFromFavorites(pet: PetModel) {
        userDataRepository.UnLike(pet)
    }

    private fun requestPage(onSuccess: (List<PetModel>) -> Unit, onError: (Throwable) -> Unit) {
        val job = petRepository.getPage(currentPage + 1)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { currentPage++ }
            .subscribe(onSuccess, onError)
        compositeDisposable.add(job)
    }

    private fun receiveUpdate(value: List<PetModel>, type: String?, breed: String?) {
        _viewState.onNext(
            MainViewState(
                updating = false,
                petList = value,
                requestType = type,
                requestBreed = breed,
                error = null
            )
        )
        refreshNeeded = value.isEmpty()
        noMorePages = value.isEmpty()
    }

    private fun receivePage(state: MainViewState, page: List<PetModel>) {
        _viewState.onNext(
            state.copy(
                updating = false,
                petList = state.petList + page,
                error = null
            )
        )
        noMorePages = page.isEmpty()
    }

    private fun receivePageError(state: MainViewState, error: String) {
        _viewState.onNext(
            state.copy(
                updating = false,
                error = error
            )
        )
    }
}
