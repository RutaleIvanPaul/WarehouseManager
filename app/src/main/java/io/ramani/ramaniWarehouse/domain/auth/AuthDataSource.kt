package io.ramani.ramaniWarehouse.domain.auth

import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.reactivex.Completable
import io.reactivex.Single

interface AuthDataSource {
    fun login(phone: String, password: String): Single<UserModel>
    fun getCurrentUser(): Single<UserModel>
    fun setCurrentUser(user: UserModel): Completable
    fun logout(): Completable
    fun refreshAccessToken(token: String): Completable
    fun getCurrentWarehouse():Single<WarehouseModel>
}