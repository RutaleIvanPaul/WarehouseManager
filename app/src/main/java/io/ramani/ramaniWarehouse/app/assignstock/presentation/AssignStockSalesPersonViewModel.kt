package io.ramani.ramaniWarehouse.app.assignstock.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.model.SalesPersonRVModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation.StockAssignmentReportViewModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.GetSalesPersonRequestModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class AssignStockSalesPersonViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val getSalesPeopleUsecase: BaseSingleUseCase<List<SalesPersonModel>, GetSalesPersonRequestModel>,
    private val salespersonRVMapper: ModelMapper<SalesPersonModel, SalesPersonRVModel>,
    private val dateFormatter: DateFormatter
) : BaseViewModel(
    application, stringProvider, sessionManager
) {

    companion object {
        val salesPeopleList = mutableListOf<SalesPersonRVModel>()
        val onSalesPeopleLoadedLiveData = MutableLiveData<Boolean>()
        val onStockTakenDateSelectedLiveData = MutableLiveData<Boolean>()
        val selectedSalespersonLiveData = MutableLiveData<String>()
        val dateStockTakenLiveData = MutableLiveData<String>()
    }

    override fun start(args: Map<String, Any?>) {
        TODO("Not yet implemented")
    }


    fun getSalespeople() {
        sessionManager.getLoggedInUser().subscribeBy {
            AssignStockViewModel.assignedItemDetails.storekeeperName = it.userName
            val single = getSalesPeopleUsecase.getSingle(GetSalesPersonRequestModel(it.companyId))
            subscribeSingle(single,
                onSuccess = {
                    isLoadingVisible = false
                    salesPeopleList.clear()
                    salesPeopleList.addAll(
                        it.mapFromWith(salespersonRVMapper).toMutableList()
                    )
                    onSalesPeopleLoadedLiveData.postValue(true)
                }, onError = {
                    isLoadingVisible = false
                    notifyError(
                        it.message
                            ?: getString(R.string.an_error_has_occured_please_try_again),
                        PresentationError.ERROR_TEXT
                    )
                })
        }
    }

    fun onSalesPersonSelected(selectedSalespersonRV: SalesPersonRVModel) {
        salesPeopleList.map {
            it.isSelected = it.id == selectedSalespersonRV.id
        }
        selectedSalespersonLiveData.postValue(selectedSalespersonRV.name!!)
        AssignStockViewModel.selectedSalespersonLiveData.postValue(selectedSalespersonRV.name!!)
        AssignStockViewModel.assignedItemDetails.salespersonName = selectedSalespersonRV.name!!
        AssignStockViewModel.assignedItemDetails.salespersonUuid = selectedSalespersonRV.id!!
        StockAssignmentReportViewModel.selectedSalesPersonId.postValue(selectedSalespersonRV.id!!)
        StockAssignmentReportViewModel.selectedSalesPersonName.postValue(selectedSalespersonRV.name!!)

    }

    fun updateStockTakenDateItem(value: Boolean) {
        onStockTakenDateSelectedLiveData.postValue(value)

    }

    fun getDate(timInMillis: Long): String =
        dateFormatter.convertToDateWithDashes1(timInMillis)

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getSalesPeopleUsecase: BaseSingleUseCase<List<SalesPersonModel>, GetSalesPersonRequestModel>,
        private val salespersonRVMapper: ModelMapper<SalesPersonModel, SalesPersonRVModel>,
        private val dateFormatter: DateFormatter
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AssignStockSalesPersonViewModel::class.java)) {
                return AssignStockSalesPersonViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getSalesPeopleUsecase,
                    salespersonRVMapper,
                    dateFormatter
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}