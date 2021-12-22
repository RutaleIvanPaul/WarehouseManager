package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.common.network.HeadersProvider
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class ConfirmReceiveViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val headersProvider: HeadersProvider
) : BaseViewModel(application, stringProvider, sessionManager) {
    var token = ""
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            token = it.token
        }
    }

    fun getUrl(purchaseId: String?): Pair<String,Map<String,String>> {
        val url =
            BuildConfig.BASE_URL.plus("purchase/order/get/invoice/for/distributor/pdf?purchaseOrderId=$purchaseId")
       var map = mapOf<String,String>()
        headersProvider.getHeaders().entries.forEach { (key, value) ->
            map += key to value
        }
        return  Pair(url,map)
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val headersProvider: HeadersProvider
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfirmReceiveViewModel::class.java)) {
                return ConfirmReceiveViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    headersProvider
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}