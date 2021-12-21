package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class ConfirmReceiveViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager
) : BaseViewModel(application, stringProvider, sessionManager) {
    var token = ""
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            token = it.token
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfirmReceiveViewModel::class.java)) {
                return ConfirmReceiveViewModel(
                    application,
                    stringProvider,
                    sessionManager
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}