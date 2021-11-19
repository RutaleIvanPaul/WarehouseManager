package io.ramani.ramaniWarehouse.app.auth.di

import io.ramani.ramaniWarehouse.data.auth.AuthApi
import io.ramani.ramaniWarehouse.data.auth.AuthLocalDataSource
import io.ramani.ramaniWarehouse.data.auth.AuthRemoteDataSource
import io.ramani.ramaniWarehouse.data.auth.AuthRepository
import io.ramani.ramaniWarehouse.data.auth.mappers.UserRemoteMapper
import io.ramani.ramaniWarehouse.data.auth.model.UserRemoteModel
import io.ramani.ramaniWarehouse.data.common.network.ServiceHelper
import io.ramani.ramaniWarehouse.domain.auth.AuthDataSource
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.manager.SessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val authDataModule = Kodein.Module("authDataModule") {

    bind<AuthApi>() with provider {
        ServiceHelper.createService<AuthApi>(instance())
    }

    bind<AuthDataSource>("authDataSource") with singleton {
        AuthRepository(
            instance("remoteAuthDataSource"),
            instance("localAuthDataSource")
        )
    }

    bind<AuthDataSource>("remoteAuthDataSource") with singleton {
        AuthRemoteDataSource(
            instance(),
            instance()
        )
    }

    bind<AuthDataSource>("localAuthDataSource") with singleton {
        AuthLocalDataSource(
            instance()
        )
    }

    bind<ModelMapper<UserRemoteModel, UserModel>>() with provider {
        UserRemoteMapper()
    }

    bind<ISessionManager>() with singleton {
        SessionManager(instance("authDataSource"))
    }
}