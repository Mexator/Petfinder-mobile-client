package com.mexator.petfinder_client.mvvm.viewmodel.liked_pets

import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.mvvm.viewstate.LikedPetsViewState
import com.mexator.petfinder_client.ui.petlist.PetHolder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * [ViewModel] for screen where user can see list of pets they put "like" on.
 * @property viewState Represents reactive state of View that is exposed by [LikedPetViewModel].
 * Shouldn't be empty
 */
class LikedPetViewModel : ViewModel(), KoinComponent {
    private val _viewState: BehaviorSubject<LikedPetsViewState> = BehaviorSubject.create()
    val viewState: Observable<LikedPetsViewState>
        get() = _viewState

    // Dependencies
    private val userDataRepository: UserDataRepository by inject()

    private val compositeDisposable = CompositeDisposable()

    init {
        _viewState.onNext(
            LikedPetsViewState(
                emptyList(),
                true
            )
        )
    }

    /**
     * Clear [compositeDisposable] when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
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
     * Load next page from repository and update viewState according to newly arrived page
     */
    fun loadNextPage() {
        _viewState.onNext(_viewState.value!!.copy(updating = true))

        val job =
            userDataRepository
                .getFavorites()
                .subscribeOn(Schedulers.io())
                .subscribe { value ->
                    val tmp = value.map { PetHolder(it, true) }
                    _viewState.onNext(LikedPetsViewState(tmp, false))
                }
        compositeDisposable.add(job)
    }
}