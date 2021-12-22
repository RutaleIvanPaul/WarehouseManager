package io.ramani.ramaniWarehouse.app.returnstock.presentation.host

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.model.ReturnItemDetails
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem
import io.ramani.ramaniWarehouse.data.returnStock.model.OfProducts
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
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

class ReturnStockViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val postReturnedStockUseCase: BaseSingleUseCase<PostReturnItemsResponse, PostReturnItems>,
    private val dateFormatter: DateFormatter,
    private val returnItemsMapper: ModelMapper<AvailableProductItem, OfProducts>
) : BaseViewModel(
    application, stringProvider, sessionManager
) {
    companion object {
        var allowToGoNext = MutableLiveData<Pair<Int, Boolean>>()
        val returnItemDetails: ReturnItemDetails = ReturnItemDetails()
        val returnItemsChangedLiveData = MutableLiveData<Boolean>()
        var signedLiveData = MutableLiveData<Pair<String, Bitmap>>()
    }

    var userModel: UserModel? = null
    var warehouseModel: WarehouseModel? = null
    val onItemsReturnedLiveData = MutableLiveData<Boolean>()

    override fun start(args: Map<String, Any?>) {
        sessionManager.getCurrentWarehouse().subscribeBy {
            warehouseModel = it
        }
        sessionManager.getLoggedInUser().subscribeBy {
            userModel = it
        }
    }

    fun returnStock() {
        val returnItems = returnItemDetails
        val single = postReturnedStockUseCase.getSingle(
            PostReturnItems(
                returnItems.storekeeperName,
                "",
                userModel!!.companyId,
                dateFormatter.convertToCalendarFormatDate(now()),
                returnItems.returnItems.mapFromWith(returnItemsMapper),
                returnItems.salespersonName,
                userModel!!.uuid,
                warehouseModel!!.id!!,
                "return",
                returnItems.signatureInfoStoreKeeper,
                returnItems.signatureInfoSalesPerson
            )
        )
        subscribeSingle(
            single,
            onSuccess = {
                isLoadingVisible = false
                onItemsReturnedLiveData.postValue(true)
            }, onError = {
                isLoadingVisible = false
                notifyError(
                    it.message
                        ?: getString(R.string.an_error_has_occured_please_try_again),
                    PresentationError.ERROR_TEXT
                )
                onItemsReturnedLiveData.postValue(false)

            }
        )

    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val postReturnedStockUseCase: BaseSingleUseCase<PostReturnItemsResponse, PostReturnItems>,
        private val dateFormatter: DateFormatter,
        private val returnItemsMapper: ModelMapper<AvailableProductItem, OfProducts>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReturnStockViewModel::class.java)) {
                return ReturnStockViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    postReturnedStockUseCase,
                    dateFormatter,
                    returnItemsMapper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}