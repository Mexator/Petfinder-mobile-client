package com.mexator.petfinder_client.data

import com.mexator.petfinder_client.data.pojo.Pet
import com.mexator.petfinder_client.data.pojo.User
import com.mexator.petfinder_client.network.NetworkService
import com.mexator.petfinder_client.network.api_interaction.CookieHolder
import com.mexator.petfinder_client.network.api_interaction.PetfinderUserAPI
import com.mexator.petfinder_client.storage.Breed
import com.mexator.petfinder_client.storage.Type
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

    override fun getUser(): Single<User> {
        return petfinderUserAPI.getMe("PFSESSION=${CookieHolder.userCookie}")
            .map { it.user }
    }


}
