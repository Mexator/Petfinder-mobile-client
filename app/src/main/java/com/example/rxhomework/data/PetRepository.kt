package com.example.rxhomework.data

import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.data.pojo.Breed
import com.example.rxhomework.data.pojo.PetEntity
import com.example.rxhomework.data.pojo.Type
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
                    val ret = remoteDataSource.getPets(type, breed)
                    localDataSource.savePets(ret)
                    ret
                } else {
                    localDataSource.getPets(type, breed)
                }
            }
    }
}
