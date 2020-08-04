package com.mexator.petfinder_client.data.actual

import android.graphics.drawable.Drawable
import android.util.Log
import com.mexator.petfinder_client.data.PetDataSource
import com.mexator.petfinder_client.data.PetRepository
import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.remote.pojo.PetResponse
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import com.mexator.petfinder_client.data.remote.pojo.User
import com.mexator.petfinder_client.extensions.getTag
import com.mexator.petfinder_client.network.NetworkService
import com.mexator.petfinder_client.data.remote.api_interaction.CookieHolder
import com.mexator.petfinder_client.data.remote.api_interaction.PetfinderUserAPI
import com.mexator.petfinder_client.data.local.PetEntity
import io.reactivex.Single
import okhttp3.MultipartBody
import org.koin.core.KoinComponent
import org.koin.core.inject

class ActualRepository(
    private val remoteDataSource: RemotePetDataSource,
    private val localDataSource: LocalPetDataSource
) : PetRepository, UserDataRepository, KoinComponent {
    // Dependencies
    private val networkService: NetworkService by inject()
    private val petfinderUserAPI: PetfinderUserAPI by inject()

    // State variables
    private var parameters: SearchParameters = SearchParameters(null, null)
    private var useRemoteSource: Boolean? = null

    override fun submitQuery(params: SearchParameters) {
        parameters = params.copy()
        useRemoteSource = null
    }

    override fun getPage(page: Int): Single<List<PetModel>> {
        return when (useRemoteSource) {
            null -> loadFirstPage()
            true -> remoteDataSource.getPets(parameters, page)
            false -> localDataSource.getPets(parameters, page)
        }.map { it as List<PetModel> }
    }

    override fun getPetPhotos(pet: PetModel, size: PetDataSource.PhotoSize): Single<List<Drawable>> =
        if (pet.source == PetModel.StorageLocation.REMOTE) {
            remoteDataSource.getPetPhotos(pet as PetResponse, size)
                .doOnSuccess { localDataSource.savePetPhotos(it, pet.id) }
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
        return petfinderUserAPI
            .getMe("PFSESSION=${CookieHolder.userCookie}")
            .map { it.user }
    }

    /**
     * Load first page and determine source for next ones
     */
    private fun loadFirstPage(): Single<List<PetModel>> {
        return networkService
            .isConnectedToInternet()
            .doOnSuccess {
                useRemoteSource = it
                Log.d(
                    getTag(),
                    "Using " + if (useRemoteSource!!) "remote" else "local" + " source of pets"
                )
            }
            .flatMap {
                if (it) {
                    remoteDataSource.getPets(parameters, 1)
                        .doOnSuccess { list -> localDataSource.savePets(list, true) }
                } else {
                    localDataSource.getPets(parameters, 1)
                }
            }
    }
}