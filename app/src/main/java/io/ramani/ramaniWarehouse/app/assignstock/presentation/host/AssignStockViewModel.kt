package io.ramani.ramaniWarehouse.app.assignstock.presentation.host

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
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

class AssignStockViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val postAssignedStockUseCase: BaseSingleUseCase<PostAssignedItemsResponse, PostAssignedItems>,
    private val dateFormatter: DateFormatter,
    private val assignedItemsMapper: ModelMapper<ProductsUIModel, ConfirmProducts>,
    private val prefs: PrefsManager
) : BaseViewModel(
    application, stringProvider, sessionManager
) {
    companion object {
        var allowToGoNext = MutableLiveData<Pair<Int, Boolean>>()
        val assignedItemDetails: AssignedItemDetails = AssignedItemDetails()
        val assignedItemsChangedLiveData = MutableLiveData<Boolean>()
        var signedLiveData = MutableLiveData<Pair<String, Bitmap>>()
        val pushBackToStart = MutableLiveData<Boolean>()
    }

    var userModel: UserModel? = null
    var warehouseModel: WarehouseModel? = null
    val onItemsAssignedLiveData = MutableLiveData<Boolean>()

    override fun start(args: Map<String, Any?>) {
        sessionManager.getCurrentWarehouse().subscribeBy {
            warehouseModel = it
        }
        sessionManager.getLoggedInUser().subscribeBy {
            userModel = it
        }
    }

    fun assignStock() {
        val assignedItems = assignedItemDetails
        val single = postAssignedStockUseCase.getSingle(
            PostAssignedItems(
                assignedItems.storekeeperName,
                "",
                userModel!!.companyId,
                dateFormatter.convertToCalendarFormatDate(now()),
                assignedItems.assignedItems.mapFromWith(assignedItemsMapper),
                assignedItems.salespersonName,
                userModel!!.uuid,
                warehouseModel!!.id!!,
                "assign",
                assignedItems.signatureInfoStoreKeeper,
                assignedItems.signatureInfoSalesPerson
            )
        )
        subscribeSingle(
            single,
            onSuccess = {
                isLoadingVisible = false
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

            }
        )

    }


    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val postAssignedStockUseCase: BaseSingleUseCase<PostAssignedItemsResponse, PostAssignedItems>,
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