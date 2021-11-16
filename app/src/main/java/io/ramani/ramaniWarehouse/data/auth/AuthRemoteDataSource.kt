package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.data.auth.model.LoginRequestModel
import io.ramani.ramaniWarehouse.data.auth.model.UserRemoteModel
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.domain.auth.AuthDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.reactivex.Completable
import io.reactivex.Single

class AuthRemoteDataSource(
    private val authApi: AuthApi,
    private val userRemoteMapper: ModelMapper<UserRemoteModel, UserModel>,
) : AuthDataSource, BaseRemoteDataSource() {
    override fun login(phone: String, password: String): Single<UserModel> =
        callSingle(
            authApi.login(LoginRequestModel(phone, password)).flatMap {
                Single.just(it.data?.mapFromWith(userRemoteMapper))
            }
        )


    override fun getCurrentUser(): Single<UserModel> {
        TODO("Not yet implemented")
    }

    override fun setCurrentUser(user: UserModel): Completable {
        TODO("Not yet implemented")
    }

    override fun logout(): Completable {
        TODO("Not yet implemented")
    }

    override fun refreshAccessToken(token: String): Completable {
        TODO("Not yet implemented")
    }

}