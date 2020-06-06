package com.example.rxhomework.data

import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.Type
import io.reactivex.Single

class ActualPetRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {
    override fun getPets(type: Type?, breed: Breed?): Single<List<Pet>> {
        return NetworkService
            .isConnectedToInternet()
            .flatMap {
                if (it) {
                    val ret = remoteDataSource.getPets(type, breed)
                    localDataSource.savePets(ret)
                    ret
                } else {
                    localDataSource.getPets(type, breed)
                }
            }
    }
}
