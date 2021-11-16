package io.ramani.ramaniWarehouse.app.common.di.pagination

import io.ramani.ramaniWarehouse.domain.auth.AuthDataSource
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.manager.SessionManager
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val authDataModule = Kodein.Module("authDataModule") {
    bind<AuthDataSource>("authDataSource") with singleton {
        AuthRepository(
            instance("remoteAuthDataSource"),
            instance("localAuthDataSource")
        )
    }

    bind<AuthDataSource>("remoteAuthDataSource") with singleton {
        AuthRemoteDataSource(
            instance(),
            instance(),
            instance()
        )
    }

    bind<AuthDataSource>("localAuthDataSource") with singleton {
        AuthLocalDataSource(
            instance()
        )
    }

    bind<ISessionManager>() with singleton {
        SessionManager(instance("authDataSource"))
    }
}