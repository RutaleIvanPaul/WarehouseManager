package io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.mapper.SalespersonRVMapper
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.model.SalespersonRVModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.MainNavViewModel
import io.ramani.ramaniWarehouse.data.returnStock.model.GetSalespeopleRequestModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.mappers.mapToWith
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.ramani.ramaniWarehouse.domain.returnStock.useCase.GetSalesPeopleUsecase
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class SalesPersonViewModel(application: Application,
                           stringProvider: IStringProvider,
                           sessionManager: ISessionManager,
                           private val getSalesPeopleUsecase: BaseSingleUseCase<List<SalespeopleModel>, GetSalespeopleRequestModel>,
                           private val salespersonRVMapper: ModelMapper<SalespeopleModel, SalespersonRVModel>
):BaseViewModel(
    application, stringProvider, sessionManager
                           ) {

    companion object{
        val salesPeopleList = mutableListOf<SalespersonRVModel>()
        val onSalesPeopleLoadedLiveData = MutableLiveData<Boolean>()
    }

    override fun start(args: Map<String, Any?>) {
        TODO("Not yet implemented")
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getSalesPeopleUsecase: BaseSingleUseCase<List<SalespeopleModel>, GetSalespeopleRequestModel>,
        private val salespersonRVMapper: ModelMapper<SalespeopleModel, SalespersonRVModel>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SalesPersonViewModel::class.java)) {
                return SalesPersonViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getSalesPeopleUsecase,
                    salespersonRVMapper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

    fun getSalespeople(){
        sessionManager.getLoggedInUser().subscribeBy {
            val single = getSalesPeopleUsecase.getSingle(GetSalespeopleRequestModel(it.companyId))
            subscribeSingle(single,
                onSuccess = {
                    isLoadingVisible = false
                    salesPeopleList.addAll(
                        it.mapFromWith(salespersonRVMapper).toMutableList()
                    )
                    onSalesPeopleLoadedLiveData.postValue(true)
                }, onError = {
                    isLoadingVisible = false
                    notifyError(it.message
                        ?: getString(R.string.an_error_has_occured_please_try_again), PresentationError.ERROR_TEXT)
                })
        }
    }

    fun onSalespersonSelected(id: String) {
        TODO()
    }
}