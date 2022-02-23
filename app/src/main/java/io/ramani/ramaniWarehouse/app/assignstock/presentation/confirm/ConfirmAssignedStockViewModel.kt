package io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.AssignProductsRequestModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.ConfirmProducts
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItems
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItemsResponse
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniWarehouse.domainCore.printer.PrinterHelper
import io.reactivex.rxkotlin.subscribeBy

class ConfirmAssignedStockViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val postAssignedStockUseCase: BaseSingleUseCase<PostAssignedItemsResponse, AssignProductsRequestModel>,
    val dateFormatter: DateFormatter,
    private val assignedItemsMapper: ModelMapper<ProductsUIModel, ConfirmProducts>,
    private val printerHelper: PrinterHelper

) : BaseViewModel(application, stringProvider, sessionManager) {

    var userModel: UserModel? = null
    val onItemsAssignedLiveData = MutableLiveData<Boolean>()
    val loadedUserDetails = MutableLiveData<UserModel>()
    val loadingIndicatorForProductAssignment = MutableLiveData<Boolean>()


    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            userModel = it
            loadedUserDetails.postValue(userModel)
        }
//        printerHelper.open()
    }

    fun printBitmap(bitmap: Bitmap){
        val printBitmap = printerHelper.printBitmap(bitmap)
        if(!printBitmap.status){
            notifyErrorObserver(printBitmap.error, PresentationError.ERROR_TEXT)
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
        private val postAssignedStockUseCase: BaseSingleUseCase<PostAssignedItemsResponse, AssignProductsRequestModel>,
        val dateFormatter: DateFormatter,
        private val assignedItemsMapper: ModelMapper<ProductsUIModel, ConfirmProducts>,
        private val printerHelper: PrinterHelper
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfirmAssignedStockViewModel::class.java)) {
                return ConfirmAssignedStockViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    postAssignedStockUseCase,
                    dateFormatter,
                    assignedItemsMapper,
                    printerHelper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}