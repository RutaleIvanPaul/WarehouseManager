package io.ramani.ramaniWarehouse.app.stockreport.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.stockreport.model.DistributorDateRequestModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedItemModel
import io.ramani.ramaniWarehouse.domainCore.printer.PrinterHelper
import io.reactivex.rxkotlin.subscribeBy

class StockReportViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val prefs: PrefsManager,
    private val getDistributorDateUseCase: BaseSingleUseCase<List<DistributorDateModel>, DistributorDateRequestModel>,
    private val printerHelper: PrinterHelper
    ) : BaseViewModel(application, stringProvider, sessionManager) {
    var userId = ""
    var username = ""
    var companyId = ""
    var warehouseId = ""

    var lastDate = ""
    var page = 1
    var size = 20
    var hasMore = true

    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()

    var stockList: ArrayList<DistributorDateModel> = ArrayList()
    val getDistributorDateActionLiveData = MutableLiveData<List<DistributorDateModel>>()

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            userId = it.uuid
            companyId = it.companyId
            username = it.userName
        }

        sessionManager.getCurrentWarehouse().subscribeBy {
            warehouseId = it.id ?: ""
        }

//        printerHelper.open()
    }

    @SuppressLint("CheckResult")
    fun getDistributorDate(date: String) {
        sessionManager.getCurrentWarehouse().subscribeBy {
            val warehouseId = it.id ?: ""

            // If date is changed, list should be cleared
            if (date != lastDate) {
                hasMore = true
                stockList.clear()
            }

            if (hasMore) {
                isLoadingVisible = true

                val single = getDistributorDateUseCase.getSingle(
                    DistributorDateRequestModel(
                        companyId /* "601ffa4d279d812ed25a7f9b" */,
                        warehouseId /* "618cdad48f172b7b7e399349" */,
                        date/* "2021-10-19" */, page, size
                    )
                )

                subscribeSingle(single, onSuccess = {
                    isLoadingVisible = false

                    hasMore = it.isNotEmpty() && it.size >= size

                    if (it.isNotEmpty()) {
                        stockList.addAll(it)
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
        }
    }

    fun printBitmap(bitmap: Bitmap){
        printerHelper.printBitmap(bitmap)
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val prefs: PrefsManager,
        private val getDistributorDateUseCase: BaseSingleUseCase<List<DistributorDateModel>, DistributorDateRequestModel>,
        private val printerHelper: PrinterHelper
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockReportViewModel::class.java)) {
                return StockReportViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    prefs,
                    getDistributorDateUseCase,
                    printerHelper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

}