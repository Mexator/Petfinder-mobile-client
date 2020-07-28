package com.mexator.petfinder_client.data.actual

import android.graphics.drawable.Drawable
import com.mexator.petfinder_client.data.DataSource
import com.mexator.petfinder_client.data.Repository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.pojo.PetResponse
import com.mexator.petfinder_client.data.pojo.User
import com.mexator.petfinder_client.network.NetworkService
import com.mexator.petfinder_client.network.api_interaction.CookieHolder
import com.mexator.petfinder_client.network.api_interaction.PetfinderUserAPI
import com.mexator.petfinder_client.storage.PetEntity
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

    override fun getPets(animalType: String?, animalBreed: String?, page: Int?): Single<List<PetModel>> {
        return networkService
            .isConnectedToInternet()
            .flatMap {
                if (it) {
                    remoteDataSource.getPets(
                        animalType,
                        animalBreed,
                        page ?: 1
                    ).doOnSuccess { list -> localDataSource.savePets(list) }
                } else {
                    localDataSource.getPets(animalType, animalBreed)
                }
            }
    }

    override fun getPetPhotos(pet: PetModel, size: DataSource.PhotoSize): Single<List<Drawable>> =
        if (pet.source == PetModel.StorageLocation.REMOTE) {
            remoteDataSource.getPetPhotos(pet as PetResponse, size)
        } else
            localDataSource.getPetPhotos(pet as PetEntity, size)


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
