package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.data.common.network.HeadersProvider
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.Params
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy
import java.io.File

class ConfirmReceiveViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val headersProvider: HeadersProvider,
    private val declineReasonsUseCase: BaseSingleUseCase<List<String>, Params>,
    private val postGoodsReceivedUseCase: BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>,
    private val dateFormatter: DateFormatter
) : BaseViewModel(application, stringProvider, sessionManager) {
    var token = ""
    var storeKeeperName = ""
    var loggedInUser = UserModel()
    var currentWarehoues = WarehouseModel()
    val postGoodsReceivedActionLiveData = MutableLiveData<GoodsReceivedModel>()
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            token = it.token
            storeKeeperName = it.userName
            loggedInUser = it
        }

        sessionManager.getCurrentWarehouse().subscribeBy {
            currentWarehoues = it
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

    private fun getDeclineReasons() {
        if (RECEIVE_MODELS.declineReasons.isEmpty()) {
            val single = declineReasonsUseCase.getSingle()
            subscribeSingle(single, onSuccess = {
                RECEIVE_MODELS.declineReasons.clear()
                RECEIVE_MODELS.declineReasons.addAll(it)
            }, onError = {})
        }
    }

    fun validateQty(quantity: Double?, acceptedQty: Double, declinedQty: Double): Boolean =
        quantity!! == (acceptedQty + declinedQty)

    /**
     * Post Goods Received
     */
    fun postGoodsReceived(storeKeeperSignature: File?, deliveryPersonSignature: File?) {
        if (storeKeeperSignature == null) {
            notifyErrorObserver(
                stringProvider.getString(R.string.missing_store_keeper_signature),
                PresentationError.ERROR_TEXT
            )
        } else if (deliveryPersonSignature == null) {
            notifyErrorObserver(
                stringProvider.getString(R.string.missing_delivery_person_signature),
                PresentationError.ERROR_TEXT
            )
        } else {
            isLoadingVisible = true
            val request = GoodsReceivedRequestModel(
                RECEIVE_MODELS.invoiceModelView?.invoiceId ?: "",
                loggedInUser.uuid,
                currentWarehoues.id ?: "",
                RECEIVE_MODELS.invoiceModelView?.distributorId ?: "",
                RECEIVE_MODELS.invoiceModelView?.serverCreatedAtDateTime ?: "",
                dateFormatter.getServerTimeFromServerDate(RECEIVE_MODELS.invoiceModelView?.serverCreatedAtDateTime),
                RECEIVE_MODELS.invoiceModelView?.deliveryPersonName ?: "",
                RECEIVE_MODELS.invoiceModelView?.supplierId,
                RECEIVE_MODELS.invoiceModelView?.products,
                storeKeeperSignature,
                deliveryPersonSignature
            )
            val single = postGoodsReceivedUseCase.getSingle(request)
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

            postGoodsReceivedActionLiveData.postValue(it)
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(
                    it.message
                        ?: stringProvider.getString(R.string.an_error_has_occured_please_try_again),
                    PresentationError.ERROR_TEXT
                )
            })
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val headersProvider: HeadersProvider,
        private val declineReasonsUseCase: BaseSingleUseCase<List<String>, Params>,
        private val postGoodsReceivedUseCase: BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>,
        private val dateFormatter: DateFormatter
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfirmReceiveViewModel::class.java)) {
                return ConfirmReceiveViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    headersProvider,
                    declineReasonsUseCase,
                    postGoodsReceivedUseCase,
                    dateFormatter
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}