package com.example.rxhomework.data

import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.Type
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class ActualPetRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository, KoinComponent {

    private val networkService: NetworkService by inject()

    override fun getPets(animalType: Type?, animalBreed: Breed?, page:Int?): Single<List<Pet>> {
        return networkService
            .isConnectedToInternet()
            .flatMap {
                if (it) {
                    val ret = remoteDataSource.getPets(animalType, animalBreed, page?: 1)
//                    localDataSource.savePets(ret)
                    ret
                } else {
                    localDataSource.getPets(animalType, animalBreed)
                }
            }
    }
}
