package io.ramani.ramaniWarehouse.data.auth

import com.google.gson.Gson
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.domain.auth.AuthDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.ramani.ramaniWarehouse.domainCore.prefs.Prefs
import io.reactivex.Completable
import io.reactivex.Single

class AuthLocalDataSource(
    private val prefsManager: Prefs
) : AuthDataSource, BaseRemoteDataSource() {
    override fun login(phone: String, password: String): Single<UserModel> {
        TODO("Not yet implemented")
    }


    override fun getCurrentUser(): Single<UserModel> =
        if (prefsManager.currentUser.isNullOrBlank()) {
            Single.just(UserModel())
        } else {
            Single.just(Gson().fromJson(prefsManager.currentUser, UserModel::class.java))
        }

    override fun setCurrentUser(user: UserModel): Completable =
        Completable.fromAction {
            prefsManager.currentUser = Gson().toJson(user)
        }

    override fun logout(): Completable =
        Completable.fromAction {
            prefsManager.currentUser = ""
            prefsManager.accessToken = ""
        }

    override fun refreshAccessToken(token: String): Completable =
        Completable.fromAction {
            prefsManager.accessToken = token
        }

    override fun getCurrentWarehouse(): Single<WarehouseModel> =
        Single.just(Gson().fromJson(prefsManager.currentWarehouse, WarehouseModel::class.java))


}