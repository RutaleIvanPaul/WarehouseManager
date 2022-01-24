package io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.model.SalesPersonRVModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.data.stockassignment.model.GetSalesPersonRequestModel
import io.ramani.ramaniWarehouse.data.stockassignmentreport.model.StockAssignmentReportDistributorDateRequestModel
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.ProductReceivedItemModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import io.ramani.ramaniWarehouse.domainCore.printer.PrinterHelper
import io.reactivex.rxkotlin.subscribeBy

class StockAssignmentReportViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val prefs: PrefsManager,
    private val printerHelper: PrinterHelper,
    private val getDistributorDateUseCase: BaseSingleUseCase<List<StockAssignmentReportDistributorDateModel>, StockAssignmentReportDistributorDateRequestModel>,
    private val getSalesPeopleUsecase: BaseSingleUseCase<List<SalesPersonModel>, GetSalesPersonRequestModel>,
    private val salespersonRVMapper: ModelMapper<SalesPersonModel, SalesPersonRVModel>

    ) : BaseViewModel(application, stringProvider, sessionManager) {
    var userId = ""
    var companyId = ""
    var companyName = ""
    var userName = ""
    var warehouseId = ""

    var lastDate = ""

    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
    val nameOfCompany = MutableLiveData<String>()

    var stockList: ArrayList<StockAssignmentReportDistributorDateModel> = ArrayList()
    var getDistributorDateActionLiveData = MutableLiveData<List<StockAssignmentReportDistributorDateModel>>()

    companion object {
        var returnSelected = MutableLiveData<Boolean>()
        val selectedSalesPersonId = MutableLiveData<String>()
        val selectedSalesPersonName = MutableLiveData<String>()

    }


    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        /*
        val user = getLoggedInUser()
        subscribeSingle(user, onSuccess = {
            getSuppliers(it.companyId, 1, 100)
        })
        */
        sessionManager.getLoggedInUser().subscribeBy {
            userId = it.uuid
            companyId = it.companyId
            companyName = it.companyName
            userName = it.userName
            nameOfCompany.postValue(it.companyName)

        }

        sessionManager.getCurrentWarehouse().subscribeBy {
            warehouseId = it.id ?: ""
        }

//        printerHelper.open()



    }

    fun getSalespeople() {
        sessionManager.getLoggedInUser().subscribeBy {
            AssignStockViewModel.assignedItemDetails.storekeeperName = it.userName
            val single = getSalesPeopleUsecase.getSingle(GetSalesPersonRequestModel(it.companyId))
            subscribeSingle(single,
                onSuccess = {
                    isLoadingVisible = false
                    AssignmentReportSalesPersonViewModel.salesPeopleList.addAll(
                        it.mapFromWith(salespersonRVMapper).toMutableList()
                    )
                    AssignmentReportSalesPersonViewModel.onSalesPeopleLoadedLiveData.postValue(true)
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


    fun getDistributorDate(startDate: String, endDate: String, isOnlyAccepted: Boolean) {
        // If date is changed, list should be cleared

            isLoadingVisible = true
             Log.e("2222222", selectedSalesPersonId.value.toString())

            val single = getDistributorDateUseCase.getSingle(
                StockAssignmentReportDistributorDateRequestModel(
                    selectedSalesPersonId.value.toString() /* "618cdad48f172b7b7e399349" */,
                    warehouseId,
                    startDate,
                    endDate
                )
            )

            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false
                getDistributorDateActionLiveData.postValue(emptyList())

                prefs.invalidate_cache_assignments_reports = true


                if (it.isNotEmpty()) {
                    for (stock in it.reversed().distinct()) {
                        val newStock = StockAssignmentReportDistributorDateModel(
                            stock.id,
                            stock.assigner,
                            stock.dateStockTaken,
                            stock.comment,
                            stock.companyId,
                            stock.timestamp,
                            ArrayList(),
                            stock.name,
                            stock.__v,
                            stock.salesPersonUID,
                            stock.stockAssignmentType,
                            stock.storeKeeperSignature,
                            stock.salesPersonSignature
                        )

                        val availableItems = ArrayList<ProductReceivedItemModel>()
                        for (item in stock.listOfProducts) {
                            if ((isOnlyAccepted && item.quantity > 0) || (!isOnlyAccepted && item.quantity > 0)) {
                                availableItems.add(item)
                            }
                        }

                        if (availableItems.isNotEmpty()) {
                            newStock.listOfProducts = availableItems
                            stockList.add(newStock)
                        }
                    }
                }


                getDistributorDateActionLiveData.postValue(stockList)

            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(
                    it.message
                        ?: getString(R.string.an_error_has_occured_please_try_again),
                    PresentationError.ERROR_TEXT
                )
            })

    }

    fun printBitmap(bitmap: Bitmap?){
        val printBitmap = bitmap?.let { printerHelper.printBitmap(it) }
        if(!printBitmap?.status!!){
            printBitmap?.let { notifyErrorObserver(it.error, PresentationError.ERROR_TEXT) }
        }
    }

    fun printText(receiptText:String){
        val printText = printerHelper.printText(receiptText)
        if(!printText.status){
            notifyErrorObserver(printText.error, PresentationError.ERROR_TEXT)
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val prefs: PrefsManager,
        private val printerHelper: PrinterHelper,
        private val getDistributorDateUseCase: BaseSingleUseCase<List<StockAssignmentReportDistributorDateModel>, StockAssignmentReportDistributorDateRequestModel>,
        private val getSalesPeopleUsecase: BaseSingleUseCase<List<SalesPersonModel>, GetSalesPersonRequestModel>,
        private val salespersonRVMapper: ModelMapper<SalesPersonModel, SalesPersonRVModel>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockAssignmentReportViewModel::class.java)) {
                return StockAssignmentReportViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    prefs,
                    printerHelper,
                    getDistributorDateUseCase,
                    getSalesPeopleUsecase,
                    salespersonRVMapper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

}