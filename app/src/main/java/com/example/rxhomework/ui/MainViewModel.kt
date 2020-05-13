package com.example.rxhomework.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxhomework.ApplicationController
import com.example.rxhomework.data.PetRepository
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.PetEntity
import com.example.rxhomework.storage.Type
import io.reactivex.disposables.CompositeDisposable

class MainViewModel : ViewModel() {
    private val repository = ApplicationController.petRepository
    private val compositeDisposable = CompositeDisposable()
    var petsList = MutableLiveData<List<PetEntity>>()
    fun getPets(type: Type?, breed: Breed?) {
        repository.getPets(type, breed).subscribe({
            if(!it.isNullOrEmpty()) {
                petsList.postValue(it)
            }
            else {
                petsList.postValue(listOf())
            }
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
