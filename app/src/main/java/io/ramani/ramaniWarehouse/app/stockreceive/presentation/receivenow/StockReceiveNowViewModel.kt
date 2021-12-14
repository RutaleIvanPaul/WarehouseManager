package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRequestModel
import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedSupplierDataModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierProductModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.Params
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SignatureInfo
import java.util.*
import kotlin.collections.ArrayList

class StockReceiveNowViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val prefs: PrefsManager,
    private val getSupplierUseCase: BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>,
    private val getDeclineReasonsUseCase: BaseSingleUseCase<List<String>, Params>,
    private val postGoodsReceivedUseCase: BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>

) : BaseViewModel(application, stringProvider, sessionManager) {
    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()

    val getSuppliersActionLiveData = MutableLiveData<List<SupplierModel>>()
    var suppliers = ArrayList<SupplierModel>()

    val getDeclineReasonsActionLiveData = MutableLiveData<List<String>>()

    val postGoodsReceivedActionLiveData = MutableLiveData<GoodsReceivedModel>()

    var companyId = ""

    override fun start(args: Map<String, Any?>) {
        /*
        val user = getLoggedInUser()
        subscribeSingle(user, onSuccess = {
            getSuppliers(it.companyId, 1, 100)
        })
        */
        notifyErrorObserver(getString(R.string.an_error_has_occured_please_try_again), PresentationError.ERROR_TEXT)

        getSuppliers("613f3aa92aa50d36120b7c67", 1, 100)
    }

    private fun getSuppliers(companyId: String, page: Int, size: Int) {
        isLoadingVisible = true

        val single = getSupplierUseCase.getSingle(GetSupplierRequestModel(companyId, page, size))
        subscribeSingle(single, onSuccess = {
            isLoadingVisible = false

            suppliers = it as ArrayList<SupplierModel>
            getSuppliersActionLiveData.postValue(it)
        }, onError = {
            isLoadingVisible = false
            notifyErrorObserver(it.message
                ?: getString(R.string.an_error_has_occured_please_try_again), PresentationError.ERROR_TEXT)
        })
    }

    fun getDeclineReasons() {
        isLoadingVisible = true

        val single = getDeclineReasonsUseCase.getSingle()
        subscribeSingle(single, onSuccess = {
            isLoadingVisible = false

            getDeclineReasonsActionLiveData.postValue(it)
        }, onError = {
            isLoadingVisible = false
            notifyErrorObserver(it.message
                ?: getString(R.string.an_error_has_occured_please_try_again), PresentationError.ERROR_TEXT)
        })
    }

    fun getAvailableProductsForSelectedSupplier(): List<SupplierProductModel> {
        supplierData.supplier?.let {
            return it.products
        }

        return ArrayList()
    }

    /**
     * Post Goods Received
     */
    fun postGoodsReceived() {
        isLoadingVisible = true

        // Create request body
        val requestBody = supplierData.createRequestBody(
            prefs.currentWarehouse,
            prefs.currentUser,
            "613f3aa92aa50d36120b7c67"
        )

        val single = postGoodsReceivedUseCase.getSingle(GoodsReceivedRequestModel(requestBody))
        subscribeSingle(single, onSuccess = {
            //isLoadingVisible = false

            postGoodsReceivedActionLiveData.postValue(it)
        }, onError = {
            //isLoadingVisible = false
            notifyErrorObserver(it.message
                ?: getString(R.string.an_error_has_occured_please_try_again), PresentationError.ERROR_TEXT)
        })
    }

    /**
     * Set any data
     */
    fun setData(what: Int, value: Any) {
        when (what) {
            DATA_SUPPLIER -> {
                supplierData.supplier = value as SupplierModel
            }
            DATA_DATE -> supplierData.date = value as Date
            DATA_DOCUMENTS -> supplierData.documents = value as ArrayList<String>
            DATA_PRODUCTS -> supplierData.products = value as ArrayList<SelectedProductModel>

            DATA_STORE_KEEPER_DATA -> supplierData.storeKeeperData = value as SignatureInfo
            DATA_DELIVERY_PERSON_DATA -> supplierData.deliveryPersonData = value as SignatureInfo
        }
    }

    fun clearData() {
        // Clear global data
        supplierData.clear()
        allowToGoNextLiveData = MutableLiveData<Pair<Int, Boolean>>()
        updateProductRequestLiveData = MutableLiveData<SelectedProductModel>()
        updateProductCompletedLiveData = MutableLiveData<SelectedProductModel>()
        signedLiveData = MutableLiveData<Pair<String, Bitmap>>()

        System.gc()
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val prefs: PrefsManager,
        private val getSupplierUseCase: BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>,
        private val getDeclineReasonsUseCase: BaseSingleUseCase<List<String>, Params>,
        private val goodsReceivedUseCase: BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockReceiveNowViewModel::class.java)) {
                return StockReceiveNowViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    prefs,
                    getSupplierUseCase, getDeclineReasonsUseCase, goodsReceivedUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

    companion object {
        const val DATA_SUPPLIER = 1000
        const val DATA_DOCUMENTS = 1001
        const val DATA_DATE = 1002
        const val DATA_PRODUCTS = 1003
        const val DATA_STORE_KEEPER_DATA = 1004
        const val DATA_DELIVERY_PERSON_DATA = 1005

        // Selected Supplier Data
        var supplierData = SelectedSupplierDataModel()
        var allowToGoNextLiveData = MutableLiveData<Pair<Int, Boolean>>()      // Allow event to go next on each page
        var updateProductRequestLiveData = MutableLiveData<SelectedProductModel>()      // Update product request
        var updateProductCompletedLiveData = MutableLiveData<SelectedProductModel>()      // Update product request

        var signedLiveData = MutableLiveData<Pair<String, Bitmap>>()      // Event triggered when sign is completed

    }
}