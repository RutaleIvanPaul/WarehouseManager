package io.ramani.ramaniWarehouse.app.main.presentation

import android.app.Application
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by Amr on 9/18/17.
 */
class MainViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager
) :
    BaseViewModel(application, stringProvider, sessionManager) {
    var isUserLoggedInBefore = false
    override fun start(args: Map<String, Any?>) {
        getLoggedInUser().subscribeBy {
            isUserLoggedInBefore = !it.uuid.isNullOrBlank()
        }
    }


}