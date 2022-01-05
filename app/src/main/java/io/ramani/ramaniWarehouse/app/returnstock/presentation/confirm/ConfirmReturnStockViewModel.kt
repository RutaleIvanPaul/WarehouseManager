package io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem
import io.ramani.ramaniWarehouse.data.returnStock.model.OfProducts
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider

class ConfirmReturnStockViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val postReturnedStockUseCase: BaseSingleUseCase<PostReturnItemsResponse, PostReturnItems>,
    private val dateFormatter: DateFormatter,
    private val returnItemsMapper: ModelMapper<AvailableProductItem, OfProducts>
) : BaseViewModel(application, stringProvider, sessionManager) {

    var userModel: UserModel? = null
    var warehouseModel: WarehouseModel? = null
    val onItemsReturnedLiveData = MutableLiveData<Boolean>()
    override fun start(args: Map<String, Any?>) {
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
            if (modelClass.isAssignableFrom(ConfirmReturnStockViewModel::class.java)) {
                return ConfirmReturnStockViewModel(
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