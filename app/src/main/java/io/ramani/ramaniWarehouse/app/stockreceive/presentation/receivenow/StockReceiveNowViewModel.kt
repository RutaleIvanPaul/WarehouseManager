package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRemoteModel
import io.ramani.ramaniWarehouse.data.auth.model.GetSupplierRequestModel
import io.ramani.ramaniWarehouse.data.auth.model.LoginRequestModel
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.SelectedSupplierDataModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import java.util.*

class StockReceiveNowViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val getSupplierUseCase: BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>

) : BaseViewModel(application, stringProvider, sessionManager) {
    val supplierData = SelectedSupplierDataModel()

    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
    val getSuppliersActionLiveData = MutableLiveData<List<SupplierModel>>()

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

            getSuppliersActionLiveData.postValue(it)
        }, onError = {
            isLoadingVisible = false
            notifyError(it.message
                ?: getString(R.string.an_error_has_occured_please_try_again), PresentationError.ERROR_TEXT)
        })
    }

    /**
     * Set any data
     */
    fun setData(what: Int, value: Any) {
        when (what) {
            DATA_SUPPLIER -> supplierData.supplier = value as SupplierModel
            DATA_DATE -> supplierData.date = value as Date
            DATA_DOCUMENTS -> supplierData.documents = value as List<String>
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getSupplierUseCase: BaseSingleUseCase<List<SupplierModel>, GetSupplierRequestModel>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockReceiveNowViewModel::class.java)) {
                return StockReceiveNowViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getSupplierUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

    companion object {
        val DATA_SUPPLIER = 1000
        val DATA_DOCUMENTS = 1001
        val DATA_DATE = 1002
    }
}