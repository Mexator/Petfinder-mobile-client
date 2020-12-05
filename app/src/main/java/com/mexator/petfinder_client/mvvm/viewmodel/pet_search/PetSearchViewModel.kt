package com.mexator.petfinder_client.mvvm.viewmodel.pet_search

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.PetRepository
import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import com.mexator.petfinder_client.mvvm.viewstate.PetSearchViewState
import com.mexator.petfinder_client.ui.petlist.PetHolder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * [ViewModel] for screen where user can search pets by some parameters
 * and results are displayed in form of list
 * @property viewState Represents reactive state of View that is exposed by [PetSearchViewModel].
 * Shouldn't be empty
 * @property refreshNeeded Is used to determine whether the ViewModel has actual data
 * or it should be refreshed with [reloadPetsList]
 */
class PetSearchViewModel : ViewModel(), KoinComponent {
    val viewState: Observable<PetSearchViewState> get() = _viewState

    var refreshNeeded: Boolean = true
        private set

    /* Subject that remembers last state of view and is used to emit events that
    * that viewState subscribers can see */
    private var _viewState: BehaviorSubject<PetSearchViewState> = BehaviorSubject.create()

    // Dependencies
    private val petRepository: PetRepository by inject()
    private val userDataRepository: UserDataRepository by inject()

    private val compositeDisposable = CompositeDisposable()

    // State variables.
    // noMorePages: Last loaded page was empty
    private var noMorePages: Boolean = false

    // currentPage: Last non-empty page number. Starts from 1
    private var currentPage: Int = 1

    // viewState shouldn't be empty
    init {
        _viewState.onNext(
            PetSearchViewState(
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

    /**
     * Submit [type] and [breed] to the [petRepository] as a search parameters.
     * Then request a first page.
     */
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

    /**
     * Load next page from repository and update viewState according to newly arrived page
     */
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

    /**
     * Add [pet] to list of favorites
     */
    fun addToFavorites(pet: PetModel) {
        userDataRepository.like(pet)
    }


    /**
     * Remove [pet] from list of favorites
     */
    fun removeFromFavorites(pet: PetModel) {
        userDataRepository.unLike(pet)
    }

    /**
     * Helper function used to create subscription to next page, zip it with list of favorites
     * to know for each pet whether it is favorite or not and subscribe to it
     * calling [onSuccess] in case of success, and [onError] in case of error
     */
    private fun requestPage(onSuccess: (List<PetHolder>) -> Unit, onError: (Throwable) -> Unit) {
        val zipLambda = { pets: List<PetModel>, favorites: List<Long> ->
            pets.map { PetHolder(it, it.id in favorites) }
        }
        val job = petRepository.getPage(currentPage + 1)
            .zipWith(userDataRepository.getFavoritesIDs(), zipLambda)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { currentPage++ }
            .subscribe(onSuccess, onError)
        compositeDisposable.add(job)
    }

    /**
     * Receive first page. Update viewState accordingly
     */
    private fun receiveUpdate(value: List<PetHolder>, type: String?, breed: String?) {
        _viewState.onNext(
            PetSearchViewState(
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

    /**
     * Receive n-th page. Update viewState accordingly
     */
    private fun receivePage(state: PetSearchViewState, page: List<PetHolder>) {
        _viewState.onNext(
            state.copy(
                updating = false,
                petList = state.petList + page,
                error = null
            )
        )
        noMorePages = page.isEmpty()
    }

    /**
     * Process case when getting page finished with error
     */
    private fun receivePageError(state: PetSearchViewState, error: String) {
        _viewState.onNext(
            state.copy(
                updating = false,
                error = error
            )
        )
    }
}
