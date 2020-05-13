package com.example.rxhomework.data

import com.example.rxhomework.ApplicationController
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.PetDB
import com.example.rxhomework.storage.PetEntity
import com.example.rxhomework.storage.Type
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocalDataSource : DataSource {
    private val db = PetDB.getDatabaseInstance(ApplicationController.context)
    override fun getPets(type: Type?, breed: Breed?): Single<List<PetEntity>> {
        return (if (type != null && breed != null) db.petDao().getPets(type, breed) else db.petDao()
            .getAllPets()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}
