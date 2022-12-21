package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.data.stockreceive.model.GetSupplierRequestModel
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.Params
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierProductModel
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.domainCore.printer.PrinterHelper
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.RequestBody

class StockReceiveNowViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val prefs: PrefsManager,
    private val getSupplierUseCase: BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>,
    private val getDeclineReasonsUseCase: BaseSingleUseCase<List<String>, Params>,
    private val postGoodsReceivedUseCase: BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>,
    private val printerHelper: PrinterHelper

) : BaseViewModel(application, stringProvider, sessionManager) {
    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()

    val getSuppliersActionLiveData = MutableLiveData<List<SupplierModel>>()
    var suppliers = ArrayList<SupplierModel>()

    val getDeclineReasonsActionLiveData = MutableLiveData<List<String>>()

    val postGoodsReceivedActionLiveData = MutableLiveData<GoodsReceivedModel>()

    var userId = ""
    var companyId = ""
    var companyName = ""
    var warehouseId = ""
    var userName = ""

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        /*
        val user = getLoggedInUser()
        subscribeSingle(user, onSuccess = {
            getSuppliers(it.companyId, 1, 100)
        })
        */
        sessionManager.getLoggedInUser().subscribeBy {
            userId = it.uuid
            userName = it.userName
            companyId = it.companyId
            companyName = it.companyName
        }

        sessionManager.getCurrentWarehouse().subscribeBy {
            warehouseId = it.id ?: ""
        }

//        printerHelper.open()
        //postGoodsReceived()
    }

    @SuppressLint("CheckResult")
    fun getSuppliers(page: Int, size: Int) {
        sessionManager.getLoggedInUser().subscribeBy {
            isLoadingVisible = true

            val single =
                getSupplierUseCase.getSingle(GetSupplierRequestModel(it.companyId, page, size))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

                suppliers = it as ArrayList<SupplierModel>
                getSuppliersActionLiveData.postValue(it)
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(
                    it.message
                        ?: getString(R.string.an_error_has_occured_please_try_again),
                    PresentationError.ERROR_TEXT
                )
            })
        }
    }

    fun getDeclineReasons() {
        isLoadingVisible = true

        val single = getDeclineReasonsUseCase.getSingle()
        subscribeSingle(single, onSuccess = {
            isLoadingVisible = false

            getDeclineReasonsActionLiveData.postValue(it)
        }, onError = {
            isLoadingVisible = false
            notifyErrorObserver(
                it.message
                    ?: getString(R.string.an_error_has_occured_please_try_again),
                PresentationError.ERROR_TEXT
            )
        })
    }

    fun getAvailableProductsForSelectedSupplier(): List<SupplierProductModel> {
        STOCK_RECEIVE_MODEL.supplierData.supplier?.let {
            return it.products
        }

        return ArrayList()
    }

    /**
     * Post Goods Received
     */
    @SuppressLint("CheckResult")
    fun postGoodsReceived(context: Context) {
        sessionManager.getLoggedInUser().subscribeBy {
            companyId = it.companyId
            userId = it.uuid

            sessionManager.getCurrentWarehouse().subscribeBy {
                warehouseId = it.id ?: ""

                if (STOCK_RECEIVE_MODEL.supplierData.storeKeeperData?.bitmap == null) {
                    notifyErrorObserver(
                        stringProvider.getString(R.string.missing_store_keeper_signature),
                        PresentationError.ERROR_TEXT
                    )
                } else if (STOCK_RECEIVE_MODEL.supplierData.deliveryPersonData?.bitmap == null) {
                    notifyErrorObserver(
                        stringProvider.getString(R.string.missing_delivery_person_signature),
                        PresentationError.ERROR_TEXT
                    )
                } else {
                    isLoadingVisible = true

                    // Create request body
                    val requestBody: RequestBody =
                        STOCK_RECEIVE_MODEL.supplierData.createRequestBody(
                            context,
                            warehouseId,
                            userId,
                            companyId
                        )

                    val request = GoodsReceivedRequestModel(requestBody)

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
            }
        }
    }

    fun printBitmap(bitmap: Bitmap){
        printerHelper.printBitmap(bitmap)
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val prefs: PrefsManager,
        private val getSupplierUseCase: BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>,
        private val getDeclineReasonsUseCase: BaseSingleUseCase<List<String>, Params>,
        private val goodsReceivedUseCase: BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>,
        private val printerHelper: PrinterHelper
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockReceiveNowViewModel::class.java)) {
                return StockReceiveNowViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    prefs,
                    getSupplierUseCase, getDeclineReasonsUseCase, goodsReceivedUseCase,
                    printerHelper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}