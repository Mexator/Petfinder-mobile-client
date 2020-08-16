package com.mexator.petfinder_client.data.mock

import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.data.model.PetModel
import com.mexator.petfinder_client.data.model.User
import io.reactivex.Single

class MockUserDataRepository : UserDataRepository {
    override fun login(username: String, password: String): Single<Boolean> {
        return Single.just(true)
    }

    override fun logout() {
    }

    override fun getUser(): Single<User> {
        return Single.just(
            User(
                "local@user.com",
                "Local user",
                "for testing only"
            )
        )
    }

    override fun setCookie(userCookie: String) {
    }

    override fun getFavorites(): Single<List<PetModel>> {
        return Single.just(emptyList())
    }
}