package io.ramani.ramaniWarehouse.app.returnstock.presentation.products

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.model.ReturnItemDetails
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableStockReturnedListItem
import io.ramani.ramaniWarehouse.data.returnStock.model.GetAvailableStockRequestModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class SelectReturnItemsViewmodel(application: Application,
                                 stringProvider: IStringProvider,
                                 sessionManager: ISessionManager,
                                 private val getAvailableStockUsecase: BaseSingleUseCase<List<AvailableStockReturnedListItem>, GetAvailableStockRequestModel>
): BaseViewModel(application, stringProvider, sessionManager) {

    companion object{
        val missingValueLiveData = MutableLiveData<Boolean>()
    }

    val avaialableProductsListOriginal = mutableListOf<AvailableProductItem>()
    val availableProductsListLiveData = MutableLiveData<List<AvailableProductItem>>()

    override fun start(args: Map<String, Any?>) {
        TODO("Not yet implemented")
    }

    fun getAvaialableStock(){
            val single = getAvailableStockUsecase
                .getSingle(
                    GetAvailableStockRequestModel(ReturnItemDetails.salespersonUuid))
            subscribeSingle(
                single,
                onSuccess = {
                    isLoadingVisible = false

                    if(it.isNotEmpty()) {
                        avaialableProductsListOriginal.addAll(it[0].products)
                        availableProductsListLiveData.postValue(it[0].products)
                    }
                    else{
                        notifyErrorObserver(getString(R.string.no_assigned_products),PresentationError.ERROR_TEXT_RETRY)
                    }
                }, onError = {
                    isLoadingVisible = false
                    notifyError(it.message
                        ?: getString(R.string.an_error_has_occured_please_try_again), PresentationError.ERROR_TEXT)
                }
            )
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getAvailableStockUsecase: BaseSingleUseCase<List<AvailableStockReturnedListItem>, GetAvailableStockRequestModel>
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SelectReturnItemsViewmodel::class.java)) {
                return SelectReturnItemsViewmodel(
                    application,
                    stringProvider,
                    sessionManager,
                    getAvailableStockUsecase
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }

    }
}