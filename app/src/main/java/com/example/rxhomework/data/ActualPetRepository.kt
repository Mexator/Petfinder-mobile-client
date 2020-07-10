package com.example.rxhomework.data

import com.example.rxhomework.data.pojo.Pet
import com.example.rxhomework.network.NetworkService
import com.example.rxhomework.network.api_interaction.PetfinderUserAPI
import com.example.rxhomework.storage.Breed
import com.example.rxhomework.storage.Type
import io.reactivex.Single
import okhttp3.MultipartBody
import org.koin.core.KoinComponent
import org.koin.core.inject

class ActualPetRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository, KoinComponent {

    private val networkService: NetworkService by inject()
    private val petfinderUserAPI: PetfinderUserAPI by inject()

    override fun getPets(animalType: Type?, animalBreed: Breed?, page: Int?): Single<List<Pet>> {
        return networkService
            .isConnectedToInternet()
            .flatMap {
                if (it) {
                    val ret = remoteDataSource.getPets(animalType, animalBreed, page ?: 1)
                    localDataSource.savePets(ret)
                    ret
                } else {
                    localDataSource.getPets(animalType, animalBreed)
                }
            }
    }

    override fun areUserCredentialsValid(username: String, password: String): Single<Boolean> {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()

        return petfinderUserAPI.checkLogin(body)
            .map { it.success }
    }
}
