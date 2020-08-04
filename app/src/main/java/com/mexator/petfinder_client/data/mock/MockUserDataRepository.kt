package com.mexator.petfinder_client.data.mock

import com.mexator.petfinder_client.data.UserDataRepository
import com.mexator.petfinder_client.data.remote.pojo.User
import io.reactivex.Single

class MockUserDataRepository : UserDataRepository {
    override fun areUserCredentialsValid(username: String, password: String): Single<Boolean> {
        return Single.just(true)
    }

    override fun getUser(): Single<User> {
        return Single.just(User("local@user.com", "Local user", "for testing only"))
    }
}