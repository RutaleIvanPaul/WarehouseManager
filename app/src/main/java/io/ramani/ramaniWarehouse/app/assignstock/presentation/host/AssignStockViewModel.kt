package io.ramani.ramaniWarehouse.app.assignstock.presentation.host

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider

class AssignStockViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager
) : BaseViewModel(
    application, stringProvider, sessionManager
) {
    override fun start(args: Map<String, Any?>) {
        TODO("Not yet implemented")
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AssignStockViewModel::class.java)) {
                return AssignStockViewModel(
                    application,
                    stringProvider,
                    sessionManager
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}