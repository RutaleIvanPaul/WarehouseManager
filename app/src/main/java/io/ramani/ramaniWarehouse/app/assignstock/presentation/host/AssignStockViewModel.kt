package io.ramani.ramaniWarehouse.app.assignstock.presentation.host

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.model.ASSIGNMENT_RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.io.toFile
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.data.stockassignment.model.AssignProductsRequestModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.ConfirmProducts
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItems
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItemsResponse
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.ramani.ramaniWarehouse.domainCore.date.now
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy
import java.util.*

class AssignStockViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val postAssignedStockUseCase: BaseSingleUseCase<PostAssignedItemsResponse, AssignProductsRequestModel>,
    private val dateFormatter: DateFormatter,
    private val assignedItemsMapper: ModelMapper<ProductsUIModel, ConfirmProducts>,
    private val prefs: PrefsManager
) : BaseViewModel(
    application, stringProvider, sessionManager
) {
    private var calendar = Calendar.getInstance()

    companion object {
        var allowToGoNext = MutableLiveData<Pair<Int, Boolean>>()
        val assignedItemDetails: AssignedItemDetails = AssignedItemDetails
        val assignedItemsChangedLiveData = MutableLiveData<Boolean>()
        var signedLiveData = MutableLiveData<Pair<String, Bitmap>>()
        val pushBackToStart = MutableLiveData<Boolean>()
        val selectedSalespersonLiveData = MutableLiveData<String>()
//        val dateStockTakenLiveData = MutableLiveData<String>()
//        val startLoading = MutableLiveData<Boolean>()

    }

    var userModel: UserModel? = null
    var warehouseModel: WarehouseModel? = null
    val onItemsAssignedLiveData = MutableLiveData<Boolean>()
    val onItemsAssignedLiveDataError = MutableLiveData<String>()
    val onPostAssignedItemsLiveDataError = MutableLiveData<Boolean>()//

    override fun start(args: Map<String, Any?>) {
        sessionManager.getCurrentWarehouse().subscribeBy {
            warehouseModel = it
        }
        sessionManager.getLoggedInUser().subscribeBy {
            userModel = it
        }
    }

    fun assignStock(context: Context) {
        isLoadingVisible = true
        val assignedItems = assignedItemDetails


        val items = PostAssignedItems(
            assignedItems.storekeeperName,
            "",
            userModel!!.companyId,
            AssignStockSalesPersonViewModel.dateStockTakenLiveData.value!!,
            ASSIGNMENT_RECEIVE_MODELS.productsSelection.value!!.toMutableList()?.mapFromWith(assignedItemsMapper),
            assignedItems.salespersonName,
            assignedItems.salespersonUuid,
            warehouseModel!!.id!!,
            "assignment",
            assignedItems.signatureInfoStoreKeeper,
            assignedItems.signatureInfoSalesPerson,
            assignedItems.signatureInfoStoreKeeper?.toFile(context),
            assignedItems.signatureInfoSalesPerson?.toFile(context),
            getApplication()
        )

        val assignProductsRequestModel = AssignProductsRequestModel(items)


        val single = postAssignedStockUseCase.getSingle(
            assignProductsRequestModel
        )
        subscribeSingle(
            single,
            onSuccess = {
                isLoadingVisible = false
                ASSIGNMENT_RECEIVE_MODELS.productsSelectionTotalNumber.postValue(0)
                //AssignedItemDetails.clearAssignedItemDetails()
                onItemsAssignedLiveData.postValue(true)
                prefs.invalidate_cache_company_products = true
            }, onError = {

                isLoadingVisible = false
                notifyError(
                    it.message
                        ?: getString(R.string.an_error_has_occured_please_try_again),
                    PresentationError.ERROR_TEXT
                )
                onItemsAssignedLiveData.postValue(false)
                onItemsAssignedLiveDataError.postValue(getString(R.string.an_error_has_occured_with_assignment))
                onPostAssignedItemsLiveDataError.postValue(true)

            }
        )

    }



    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val postAssignedStockUseCase: BaseSingleUseCase<PostAssignedItemsResponse, AssignProductsRequestModel>,
        private val dateFormatter: DateFormatter,
        private val assignedItemsMapper: ModelMapper<ProductsUIModel, ConfirmProducts>,
        private val prefs: PrefsManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AssignStockViewModel::class.java)) {
                return AssignStockViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    postAssignedStockUseCase,
                    dateFormatter,
                    assignedItemsMapper,
                    prefs

                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}