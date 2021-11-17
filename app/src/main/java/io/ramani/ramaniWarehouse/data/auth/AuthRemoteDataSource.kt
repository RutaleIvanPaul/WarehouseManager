package io.ramani.ramaniWarehouse.data.auth

import io.ramani.ramaniWarehouse.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniWarehouse.data.auth.model.LoginRequestModel
import io.ramani.ramaniWarehouse.data.auth.model.UserRemoteModel
import io.ramani.ramaniWarehouse.data.common.network.ErrorConstants
import io.ramani.ramaniWarehouse.data.common.network.toErrorResponseModel
import io.ramani.ramaniWarehouse.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniWarehouse.domain.auth.AuthDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.entities.BaseErrorResponse
import io.ramani.ramaniWarehouse.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniWarehouse.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.HttpException

class AuthRemoteDataSource(
    private val authApi: AuthApi,
    private val userRemoteMapper: ModelMapper<UserRemoteModel, UserModel>,
) : AuthDataSource, BaseRemoteDataSource() {
    override fun login(phone: String, password: String): Single<UserModel> =
        callSingle(
            authApi.login(LoginRequestModel(phone, password)).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(userRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
                }
            }
                .onErrorResumeNext {
                    if (it is HttpException) {
                        val code = it.code()
                        val errorResponse = it.toErrorResponseModel<BaseErrorResponse<Any>>()
                        when (code) {
                            ErrorConstants.INPUT_VALIDATION_400,
                            ErrorConstants.NOT_FOUND_404 ->
                                Single.error(InvalidLoginException(errorResponse?.message))
                            ErrorConstants.NOT_AUTHORIZED_403 ->
                                Single.error(AccountNotActiveException(errorResponse?.message))
                            else -> Single.error(it)
                        }
                    } else if (it is NotAuthenticatedException) {
                        val message =
                            if (!it.message.isNullOrBlank()) it.message
                            else if (it.cause.isNotNull() && !it.cause?.message.isNullOrBlank()) it.cause?.message
                            else "Not Authorized exception"
                        Single.error(
                            NotAuthorizedException(
                                message ?: ""
                            )
                        )

                    } else {
                        Single.error(it)
                    }
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