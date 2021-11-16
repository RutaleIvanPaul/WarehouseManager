package io.ramani.ramaniWarehouse.domain.auth.useCase

import io.ramani.ramaniWarehouse.data.auth.model.LoginRequestModel
import io.ramani.ramaniWarehouse.domain.auth.AuthDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.executor.ThreadExecutor
import io.reactivex.Single

class LoginUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val authDataSource: AuthDataSource
) : BaseSingleUseCase<UserModel, LoginRequestModel>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: LoginRequestModel?): Single<UserModel> =
        authDataSource.login(params?.phoneNumber ?: "", params?.password ?: "")

}