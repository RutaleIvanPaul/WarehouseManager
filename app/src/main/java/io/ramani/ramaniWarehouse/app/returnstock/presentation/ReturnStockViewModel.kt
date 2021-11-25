package io.ramani.ramaniWarehouse.app.returnstock.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider

class ReturnStockViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager
) : BaseViewModel(
    application, stringProvider, sessionManager
) {
    override fun start(args: Map<String, Any?>) {
        TODO("Not yet implemented")
    }
}