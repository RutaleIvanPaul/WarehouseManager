package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.domain.auth.AuthDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.reactivex.Completable
import io.reactivex.Single

class AuthRepository(
    private val remoteAuthDataSource: AuthDataSource,
    private val localAuthDataSource: AuthDataSource
) :
    AuthDataSource {
    override fun login(phone: String, password: String): Single<UserModel> =
        remoteAuthDataSource.login(phone, password)


    override fun getCurrentUser(): Single<UserModel> =
        localAuthDataSource.getCurrentUser()

    override fun setCurrentUser(user: UserModel): Completable =
        localAuthDataSource.setCurrentUser(user)

    override fun logout(): Completable =
        localAuthDataSource.logout()

    override fun refreshAccessToken(token: String): Completable =
        localAuthDataSource.refreshAccessToken(token)

    override fun getCurrentWarehouse(): Single<WarehouseModel> =
        localAuthDataSource.getCurrentWarehouse()

}