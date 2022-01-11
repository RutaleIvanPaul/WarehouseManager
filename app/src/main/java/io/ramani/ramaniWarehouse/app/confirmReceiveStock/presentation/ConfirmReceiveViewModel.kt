package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowViewModel
import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.data.common.network.HeadersProvider
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.Params
import io.ramani.ramaniWarehouse.domain.stockreceive.useCase.GetDeclineReasonsUseCase
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.RequestBody

class ConfirmReceiveViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val headersProvider: HeadersProvider,
    private val declineReasonsUseCase: BaseSingleUseCase<List<String>, Params>,
    private val postGoodsReceivedUseCase: BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>
) : BaseViewModel(application, stringProvider, sessionManager) {
    var token = ""
    var storeKeeperName = ""
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            token = it.token
            storeKeeperName = it.userName
        }
        getDeclineReasons()
    }

    fun getUrl(purchaseId: String?): Pair<String, Map<String, String>> {
        val url =
            BuildConfig.BASE_URL.plus("purchase/order/get/invoice/for/distributor/pdf?purchaseOrderId=$purchaseId")
        var map = mapOf<String, String>()
        headersProvider.getHeaders().entries.forEach { (key, value) ->
            map += key to value
        }
        return Pair(url, map)
    }

    private fun getDeclineReasons(){
        if(RECEIVE_MODELS.declineReasons.isEmpty()){
            val single = declineReasonsUseCase.getSingle()
            subscribeSingle(single,onSuccess = {
                RECEIVE_MODELS.declineReasons.clear()
                RECEIVE_MODELS.declineReasons.addAll(it)
            },onError = {})
        }
    }

    fun validateQty(quantity: Double?, acceptedQty: Double, declinedQty: Double): Boolean =
        quantity!! >= (acceptedQty + declinedQty)

    /**
     * Post Goods Received

    fun postGoodsReceived() {
        isLoadingVisible = true
getLoggedInUser().subscribeBy { loggedInUser ->
    getCurrentWarehouse().subscribeBy { currentWarehouse ->

    }
}
        val request = GoodsReceivedRequestModel(
            RECEIVE_MODELS.invoiceModelView?.invoiceId?:"",
            RECEIVE_MODELS.invoiceModelView?.,
            warehouseId,
            companyId,
            "",
            "",
            "adrian"
        )
        val single = postGoodsReceivedUseCase.getSingle(request)
        subscribeSingle(single, onSuccess = {
            isLoadingVisible = false

            postGoodsReceivedActionLiveData.postValue(it)
        }, onError = {
            isLoadingVisible = false
            notifyErrorObserver(
                it.message
                    ?: getString(R.string.an_error_has_occured_please_try_again),
                PresentationError.ERROR_TEXT
            )
        })

    }
     */
    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val headersProvider: HeadersProvider,
        private val declineReasonsUseCase: BaseSingleUseCase<List<String>, Params>,
        private val postGoodsReceivedUseCase: BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfirmReceiveViewModel::class.java)) {
                return ConfirmReceiveViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    headersProvider,
                    declineReasonsUseCase,
                    postGoodsReceivedUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}