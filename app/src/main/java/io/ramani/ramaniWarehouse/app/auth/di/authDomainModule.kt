package io.ramani.ramaniWarehouse.app.auth.di

import io.ramani.ramaniWarehouse.data.auth.models.LoginRequestModel
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.auth.useCase.LoginUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val authDomainModule = Kodein.Module("authDomainModule") {
    bind<BaseSingleUseCase<UserModel, LoginRequestModel>>("loginUseCase") with provider {
        LoginUseCase(instance(), instance(), instance("authDataSource"))
    }
}