package com.example.rxhomework.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.afollestad.recyclical.datasource.emptyDataSourceTyped
import com.example.rxhomework.data.Repository
import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.Type
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    private val compositeDisposable = CompositeDisposable()

    private var updating: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private var pageLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun getUpdatingStatus() = updating as Observable<Boolean>
    fun getPageLoading() = pageLoading as Observable<Boolean>

    private var currentPage: Int = 1
    private var currentType: Type? = null
    private var currentBreed: Breed? = null

    val petDataSource = emptyDataSourceTyped<Pet>()

    fun updatePetsList(type: Type?, breed: Breed?) {
        if (!updating.hasValue() || updating.value == false) {
            updating.onNext(true)

            currentBreed = breed
            currentType = type

            val job = repository.getPets(type, breed)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { value ->
                    petDataSource.set(value)
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    petDataSource.addAll(list)
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
