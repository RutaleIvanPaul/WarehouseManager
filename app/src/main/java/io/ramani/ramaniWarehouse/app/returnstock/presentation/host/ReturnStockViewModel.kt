package io.ramani.ramaniWarehouse.app.returnstock.presentation.host

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    companion object{
        var allowToGoNext = MutableLiveData<Pair<Int, Boolean>>()
    }

    override fun start(args: Map<String, Any?>) {
        TODO("Not yet implemented")
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReturnStockViewModel::class.java)) {
                return ReturnStockViewModel(
                    application,
                    stringProvider,
                    sessionManager
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}