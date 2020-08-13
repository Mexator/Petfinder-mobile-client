package com.mexator.petfinder_client.data.actual

import android.graphics.drawable.Drawable
import android.util.Log
import com.mexator.petfinder_client.data.PetDataSource
import com.mexator.petfinder_client.data.PetRepository
import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.data.local.PetEntity
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.model.User
import com.mexator.petfinder_client.data.remote.api_interaction.CookieHolder
import com.mexator.petfinder_client.data.remote.api_interaction.PetfinderUserAPI
import com.mexator.petfinder_client.data.remote.pojo.PetResponse
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import com.mexator.petfinder_client.extensions.getTag
import com.mexator.petfinder_client.network.NetworkService
import com.mexator.petfinder_client.storage.StorageManager
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import org.koin.core.KoinComponent
import org.koin.core.inject

class ActualRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : PetRepository, UserDataRepository, KoinComponent {
    // Dependencies
    private val networkService: NetworkService by inject()
    private val petfinderUserAPI: PetfinderUserAPI by inject()
    private val storageManager: StorageManager by inject()
    private val cookieHolder: CookieHolder by inject()

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

    override fun getPetPhotos(
        pet: PetModel,
        size: PetDataSource.PhotoSize
    ): List<Single<Drawable>> =
        if (pet.source == PetModel.StorageLocation.REMOTE) {
            remoteDataSource.getPetPhotos(pet as PetResponse, size)
                .onEach { it.doOnSuccess { photo -> localDataSource.savePetPhoto(photo, pet.id) } }
        } else
            localDataSource.getPetPhotos(pet as PetEntity, size)

    override fun getPetPreview(pet: PetModel): Maybe<Drawable> =
        if (pet.source == PetModel.StorageLocation.REMOTE) {
            remoteDataSource.getPetPreview(pet as PetResponse)
        } else
            localDataSource.getPetPreview(pet as PetEntity)

    override fun login(username: String, password: String): Single<Boolean> {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()

        return petfinderUserAPI.checkLogin(body)
            .map { it.success }
            .doOnSuccess { storageManager.saveCredentials(userCookie = CookieHolder.userCookie) }
    }

    override fun logout() {
        cookieHolder.userCookie = ""
        storageManager.saveCredentials("")
        Completable.fromAction {localDataSource.deleteUser() }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun getUser(): Single<User> {
        return localDataSource.getUser(cookieHolder.userCookie)
            .onErrorResumeNext {
                remoteDataSource.getUser(cookieHolder.userCookie)
                    .doOnSuccess { localDataSource.saveUser(it) }
            }
    }

    override fun setCookie(userCookie: String) {
        cookieHolder.userCookie = userCookie
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
