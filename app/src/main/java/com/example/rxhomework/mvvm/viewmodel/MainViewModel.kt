package com.example.rxhomework.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.rxhomework.ApplicationController
import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.Type
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class MainViewModel : ViewModel() {
    val TAG = MainViewModel::class.simpleName
    private val repository = ApplicationController.actualPetRepository

    private val compositeDisposable = CompositeDisposable()

    private var petsList: BehaviorSubject<List<Pet>> = BehaviorSubject.create()
    private var updating: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private var pageLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun getPetsList() = petsList as Observable<List<Pet>>
    fun getUpdating() = updating as Observable<Boolean>
    fun getPageLoading() = pageLoading as Observable<Boolean>

    private var currentPage: Int = 1
    private var currentType: Type? = null
    private var currentBreed: Breed? = null

    fun updatePetsList(type: Type?, breed: Breed?) {
        if (!updating.hasValue() || updating.value == false) {
            updating.onNext(true)

            currentBreed = breed
            currentType = type

            val job = repository.getPets(type, breed)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { value ->
                    petsList.onNext(value ?: listOf())
                    updating.onNext(false)
                    currentPage = 1
                }
            compositeDisposable.add(job)
        }
    }

    fun loadNextPage() {
        if (!pageLoading.hasValue() || pageLoading.value == false) {
            pageLoading.onNext(true)
            val job = repository
                .getPets(currentType, currentBreed, ++currentPage)
                .subscribe { list ->
                    petsList.onNext(petsList.value.orEmpty() + list)
                    pageLoading.onNext(false)
                }
            compositeDisposable.add(job)
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
