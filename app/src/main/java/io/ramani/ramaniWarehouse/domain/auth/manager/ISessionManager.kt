package io.ramani.ramaniWarehouse.domain.auth.manager

import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.reactivex.Completable
import io.reactivex.Single

interface ISessionManager {
    fun login(email: String, password: String): Completable

    fun refreshAccessToken(token: String): Completable

    fun isUserLoggedIn(): Single<Boolean>

    fun getLoggedInUser(): Single<UserModel>

    fun logout(): Completable

    fun getCurrentWarehouse():Single<WarehouseModel>


}