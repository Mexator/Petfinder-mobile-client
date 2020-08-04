package com.mexator.petfinder_client.mvvm.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.PetDataSource
import com.mexator.petfinder_client.data.PetRepository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.mvvm.viewstate.PetDetailViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class PetDetailViewModel : ViewModel(), KoinComponent {
    private val repository: PetRepository by inject()
    private val compositeDisposable = CompositeDisposable()

    private val _viewState: BehaviorSubject<PetDetailViewState> = BehaviorSubject.create()
    val viewState: Observable<PetDetailViewState>
        get() = _viewState

    fun loadPhotos() {
        _viewState.value?.let { state ->
            val job = repository.getPetPhotos(state.petData, PetDataSource.PhotoSize.MEDIUM)
                .subscribeOn(Schedulers.io())
                .subscribe({ value -> receivePhotos(state, value) }) {}
            compositeDisposable.add(job)
        }
    }

    fun setPet(pet: PetModel) {
        _viewState.onNext(
            if (_viewState.hasValue()) {
                _viewState.value!!.copy(petData = pet)
            } else PetDetailViewState(emptyList(), 0, pet)
        )
    }

    private fun receivePhotos(state: PetDetailViewState, photos: List<Drawable>) {
        _viewState.onNext(
            state.copy(
                photos = photos,
                photoCount = photos.size
            )
        )
    }
}