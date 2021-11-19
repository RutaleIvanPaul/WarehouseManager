package io.ramani.ramaniWarehouse.domain.auth.manager

import io.ramani.ramaniWarehouse.domain.auth.AuthDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.reactivex.Completable
import io.reactivex.Single

class SessionManager(
    private val authDataSource: AuthDataSource
) : ISessionManager {
    override fun login(email: String, password: String): Completable =
        TODO("NOT YET IMPLEMENTED")

    override fun refreshAccessToken(token: String): Completable =
        authDataSource.refreshAccessToken(token)

    override fun isUserLoggedIn(): Single<Boolean> =
        authDataSource.getCurrentUser().flatMap { user ->
            Single.just(user?.uuid != null)
        }


    override fun getLoggedInUser(): Single<UserModel> =
        authDataSource.getCurrentUser()


    override fun logout(): Completable =
        authDataSource.logout()
}