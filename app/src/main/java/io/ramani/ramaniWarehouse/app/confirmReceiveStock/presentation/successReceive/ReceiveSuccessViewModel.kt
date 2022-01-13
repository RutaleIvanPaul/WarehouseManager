package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.successReceive

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider

class ReceiveSuccessViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager
):BaseViewModel(application, stringProvider, sessionManager) {
    override fun start(args: Map<String, Any?>) {
    }


    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReceiveSuccessViewModel::class.java)) {
                return ReceiveSuccessViewModel(
                    application,
                    stringProvider,
                    sessionManager
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

}