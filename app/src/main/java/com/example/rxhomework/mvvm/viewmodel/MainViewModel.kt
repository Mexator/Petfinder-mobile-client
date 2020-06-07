package com.example.rxhomework.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxhomework.ApplicationController
import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.Type
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class MainViewModel : ViewModel() {
    private val repository = ApplicationController.actualPetRepository

    private val compositeDisposable = CompositeDisposable()
    var petsList = MutableLiveData<List<Pet>>()

    private var updating: BehaviorSubject<Boolean> = BehaviorSubject.create()
    fun getUpdating():Observable<Boolean> {
        return  updating
    }

    fun getPets(type: Type?, breed: Breed?) {
        updating.onNext(true)
        repository.getPets(type, breed).subscribe({
            if(!it.isNullOrEmpty()) {
                petsList.postValue(it)
            }
            else {
                petsList.postValue(listOf())
            }
            updating.onNext(false)
        }, {}).let {
            compositeDisposable.add(it)
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
