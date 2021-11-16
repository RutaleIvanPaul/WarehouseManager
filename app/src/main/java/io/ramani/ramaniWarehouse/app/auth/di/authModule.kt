package io.ramani.ramaniWarehouse.app.auth.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.auth.presentation.LoginViewModel
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

/**
 * Forgetd by mahmoud on 9/22/17.
 */
val authModule = Module("authModule") {

    import(authDataModule)
    import(authDomainModule)




    bind<LoginViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, LoginViewModel.Factory(
                instance(), instance(), instance(), instance("loginUseCase")
            )
        ).get(LoginViewModel::class.java)
    }


}