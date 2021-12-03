package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRequestModel
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedSupplierDataModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierProductModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.v2.Params
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import java.util.*
import kotlin.collections.ArrayList

class StockReceiveNowViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val getSupplierUseCase: BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>,
    private val getDeclineReasonsUseCase: BaseSingleUseCase<List<String>, Params>

) : BaseViewModel(application, stringProvider, sessionManager) {
    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()

    val getSuppliersActionLiveData = MutableLiveData<List<SupplierModel>>()
    var suppliers = ArrayList<SupplierModel>()

    val getDeclineReasonsActionLiveData = MutableLiveData<List<String>>()

    override fun start(args: Map<String, Any?>) {
        /*
        val user = getLoggedInUser()
        subscribeSingle(user, onSuccess = {
            getSuppliers(it.companyId, 1, 100)
        })
        */

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
            notifyError(it.message
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
            notifyError(it.message
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
     * Set any data
     */
    fun setData(what: Int, value: Any) {
        when (what) {
            DATA_SUPPLIER -> {
                supplierData.supplier = value as SupplierModel
            }
            DATA_DATE -> supplierData.date = value as Date
            DATA_DOCUMENTS -> supplierData.documents = value as List<String>
            DATA_PRODUCTS -> supplierData.products = value as List<SelectedProductModel>
        }
    }

    fun clearData() {
        // Clear global data
        supplierData.clear()
        allowToGoNextLiveData = MutableLiveData<Pair<Int, Boolean>>()
        updateProductRequestLiveData = MutableLiveData<SelectedProductModel>()
        updateProductCompletedLiveData = MutableLiveData<SelectedProductModel>()

        System.gc()
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getSupplierUseCase: BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>,
        private val getDeclineReasonsUseCase: BaseSingleUseCase<List<String>, Params>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockReceiveNowViewModel::class.java)) {
                return StockReceiveNowViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getSupplierUseCase, getDeclineReasonsUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

    companion object {
        val DATA_SUPPLIER = 1000
        val DATA_DOCUMENTS = 1001
        val DATA_DATE = 1002
        val DATA_PRODUCTS = 1003

        // Selected Supplier Data
        var supplierData = SelectedSupplierDataModel()
        var allowToGoNextLiveData = MutableLiveData<Pair<Int, Boolean>>()      // Allow event to go next on each page
        var updateProductRequestLiveData = MutableLiveData<SelectedProductModel>()      // Update product request
        var updateProductCompletedLiveData = MutableLiveData<SelectedProductModel>()      // Update product request
    }
}