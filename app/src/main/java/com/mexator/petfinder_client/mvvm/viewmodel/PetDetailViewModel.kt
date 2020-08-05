package com.mexator.petfinder_client.mvvm.viewmodel

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.ViewModel
import com.mexator.petfinder_client.data.PetDataSource
import com.mexator.petfinder_client.data.PetRepository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.extensions.getTag
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
            val list = repository.getPetPhotos(state.petData, PetDataSource.PhotoSize.MEDIUM)

            for (index in list.indices) {
                val job = list[index].subscribeOn(Schedulers.io())
                    .subscribe({ value -> receivePhoto(value, index) })
                    { error -> errorOnReceivingPhoto(error) }

                compositeDisposable.add(job)
            }

            val nullList = List<Drawable?>(list.size) { null }
            _viewState.onNext(state.copy(photos = nullList, photoCount = list.size))
        }
    }

    fun setPet(pet: PetModel) {
        _viewState.onNext(
            if (_viewState.hasValue()) {
                _viewState.value!!.copy(petData = pet)
            } else PetDetailViewState(emptyList(), 0, pet)
        )
    }

    private fun receivePhoto(photo: Drawable, index: Int) {
        _viewState.value?.let {
            val newPhotoList = it.photos.toMutableList()
            newPhotoList[index] = photo

            _viewState.onNext(it.copy(photos = newPhotoList))
        }
    }

    private fun errorOnReceivingPhoto(error: Throwable) {
        Log.d(getTag(), error.localizedMessage ?: "Unknown error")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}