package com.example.rxhomework.data

import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.PetEntity
import com.example.rxhomework.storage.Type
import io.reactivex.Single

class PetRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {
    override fun getPets(type: Type?, breed: Breed?): Single<List<PetEntity>> {
        return NetworkService
            .isConnectedToInternet()
            .flatMap {
                if (it) {
                    val ret = remoteDataSource.getPets()
                    localDataSource.savePets(ret)
                    ret
                } else {
                    localDataSource.getPets()
                }
            }
    }
}
