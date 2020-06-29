package com.example.rxhomework.mvvm.viewmodel

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
    private val repository = ApplicationController.actualPetRepository

    private val compositeDisposable = CompositeDisposable()

    private var petsList: BehaviorSubject<List<Pet>> = BehaviorSubject.create()
    fun getPetsList(): Observable<List<Pet>> = petsList

    private var updating: BehaviorSubject<Boolean> = BehaviorSubject.create()
    fun getUpdating(): Observable<Boolean> = updating

    fun updatePetsList(type: Type?, breed: Breed?) {
        updating.onNext(true)
        val job = repository.getPets(type, breed)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {value ->
            petsList.onNext(value ?: listOf())
            updating.onNext(false)}
        compositeDisposable.add(job)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
