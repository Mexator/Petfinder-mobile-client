package com.mexator.petfinder_client.data.actual

import android.graphics.drawable.Drawable
import android.util.Log
import com.mexator.petfinder_client.data.PetDataSource
import com.mexator.petfinder_client.data.PetRepository
import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.data.UserDataSource
import com.mexator.petfinder_client.data.local.entity.PetEntity
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.model.User
import com.mexator.petfinder_client.data.remote.api_interaction.CookieHolder
import com.mexator.petfinder_client.data.remote.api_interaction.PetfinderUserAPI
import com.mexator.petfinder_client.data.remote.pojo.PetResponse
import com.mexator.petfinder_client.data.remote.pojo.SearchParameters
import com.mexator.petfinder_client.extensions.getTag
import com.mexator.petfinder_client.network.NetworkService
import com.mexator.petfinder_client.storage.StorageManager
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
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

    // Disposable for background jobs
    private var compositeDisposable = CompositeDisposable()

    /**
    Start of [PetRepository]-implementing functions
     */

    /**
     * Set passed search [params] as searched now
     */
    override fun submitQuery(params: SearchParameters) {
        parameters = params.copy()
        useRemoteSource = null
    }

    /**
     * Get next page of current pet search
     * @return list of pets whose parameters satisfy the query params
     */
    override fun getPage(page: Int): Single<List<PetModel>> {
        return when (useRemoteSource) {
            null -> loadFirstPage()
            true -> remoteDataSource.getPets(parameters, page)
            false -> localDataSource.getPets(parameters, page)
        }.map { it }
    }

    /**
     * Return observable list of photos of given pet. If pet is obtained from
     * remote source, its photos (if they will be successfully fetched) will
     * also be saved to local disk.
     */
    override fun getPetPhotos(
        pet: PetModel,
        size: PetDataSource.PhotoSize
    ): List<Single<Drawable>> {
        val photos: List<Single<Drawable>>

        if (pet.source == PetModel.StorageLocation.REMOTE) {
            photos = remoteDataSource.getPetPhotos(pet as PetResponse, size)
            photos.forEach {
                it.doOnSuccess { photo ->
                    localDataSource.savePetPhoto(photo, pet.id)
                }
            }
        } else {
            photos = localDataSource.getPetPhotos(pet as PetEntity, size)
        }
        return photos
    }

    /**
    Start of [UserDataRepository]-implementing functions
     */

    /**
     * @return preview photo of [pet] or `null` if it has no photos.
     */
    override fun getPetPreview(pet: PetModel): Maybe<Drawable> =
        if (pet.source == PetModel.StorageLocation.REMOTE) {
            remoteDataSource.getPetPreview(pet as PetResponse)
        } else
            localDataSource.getPetPreview(pet as PetEntity)

    /**
     * @return pet model with id [id], or null if it not exist.
     *
     * Prefer local source, fallback to remote
     */
    override fun getPet(id: Long): Maybe<PetModel> {
        val fallback = remoteDataSource.getPet(id)
            .subscribeOn(Schedulers.io()) as Maybe<PetModel>

        return (localDataSource.getPet(id) as Maybe<PetModel>)
            .subscribeOn(Schedulers.io())
            .switchIfEmpty(fallback)
            .onErrorComplete()
    }

    /**
     * Check user login and password for validity via remote backend API.
     * Save user cookie to preferences in case if login response is success.
     */
    override fun login(username: String, password: String): Single<Boolean> {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()

        return petfinderUserAPI.checkLogin(body)
            .map { it.success }
            .doOnSuccess {
                storageManager.saveCredentials(CookieHolder.userCookie)
            }
    }

    /**
     * Delete all user data from the device:
     * * User credentials,
     * * List of favorites,
     * * User associated cookie
     */
    override fun logout() {
        cookieHolder.userCookie = ""
        storageManager.saveCredentials("")
        Completable.fromAction { localDataSource.deleteUser() }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    /**
     * Get user model instance associated with current user
     */
    override fun getUser(): Single<User> {
        return localDataSource.getUser(cookieHolder.userCookie)
            .onErrorResumeNext {
                remoteDataSource.getUser(cookieHolder.userCookie)
                    .doOnSuccess { localDataSource.saveUser(it) }
            }
    }

    //TODO Remove
    override fun setCookie(userCookie: String) {
        cookieHolder.userCookie = userCookie
    }

    /**
     * Get list of IDs of favorite pets.
     *
     * Usually this function is used when it is needed to show correct state of
     * "like" checkbox. So it is not important to **always** show up-to-date
     * list of pets
     */
    override fun getFavoritesIDs(): Single<List<Long>> {
        // Get new favorites in background
        // TODO: rework this, because it is not ok to request list of favorites for every pet
        val job = remoteDataSource
            .getFavoritesIDs(cookieHolder.userCookie)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ localDataSource.updateFavorites(it) }) {}
        compositeDisposable.add(job)
        // Show locally saved ones
        return localDataSource.getFavoritesIDs("")
            .subscribeOn(Schedulers.io())
    }

    /**
     * Get list of favorite pets.
     *
     * See [getPet] and [getFavoritesIDs] to know how data sources are chosen
     *
     * @see [getPet] and
     * @see [getFavoritesIDs]
     */
    override fun getFavorites(): Single<List<PetModel>> {
        // Map ID to [Single<Notification<PetModel>>]
        val mapper = { id: Long ->
            getPet(id)
                // Could use [Maybe.materialize()] here, but it is @Experimental
                .toObservable().materialize()
                // take(1) to ignore all extra OnComplete notifications that appeared
                // because of the cast to Observable
                .take(1)
        }

        return getFavoritesIDs()
            .flatMap { list ->
                val maybes = list.map(mapper)
                // Combine all notifications to list, get values, ignore nulls
                Observable.zip(maybes) { notifications ->
                    (notifications.toList() as List<Notification<PetModel>>)
                        .mapNotNull { it.value }
                }
                    // Assert that zip results in only one value - otherwise we
                    // doing something wrong
                    .singleOrError()
            }
            .doOnSuccess { list ->
                localDataSource.savePets(list, false)
                localDataSource.updateFavorites(list.map { it.id })
            }
    }

    /**
     * Mark pet as favorite.
     * Tries to add pet to favorites to local and remote storage. Ignores errors
     */
    override fun like(pet: PetModel) {
        val sources = listOf<UserDataSource>(localDataSource, remoteDataSource)
        for (source in sources) {
            source
                .addFavorite(cookieHolder.userCookie, pet = pet)
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }

    /**
     * Mark pet as not favorite.
     * Tries to remove pet from favorites in local and remote storage. Ignores errors
     */
    override fun unLike(pet: PetModel) {
        val sources = listOf<UserDataSource>(localDataSource, remoteDataSource)
        for (source in sources) {
            source
                .removeFavorite(cookieHolder.userCookie, pet = pet)
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }

    /**
     * Load first page and determine source for next ones.
     * If remote page was successfully fetched, next time network storage will
     * be chosen
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
