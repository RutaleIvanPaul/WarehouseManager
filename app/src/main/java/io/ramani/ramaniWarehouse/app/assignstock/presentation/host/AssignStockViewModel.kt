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
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.model.ASSIGNMENT_RECEIVE_MODELS
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
        val assignedItemDetails: AssignedItemDetails = AssignedItemDetails
        val assignedItemsChangedLiveData = MutableLiveData<Boolean>()
        var signedLiveData = MutableLiveData<Pair<String, Bitmap>>()
        val pushBackToStart = MutableLiveData<Boolean>()
        val selectedSalespersonLiveData = MutableLiveData<String>()
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
        val gh =   PostAssignedItems(
            assignedItems.storekeeperName,
            "",
            userModel!!.companyId,
            dateFormatter.convertToCalendarFormatDate(now()),
            ASSIGNMENT_RECEIVE_MODELS.productsSelection.value!!.toMutableList()?.mapFromWith(assignedItemsMapper),
            assignedItems.salespersonName,
            userModel!!.uuid,
            warehouseModel!!.id!!,
            "assignment",
            assignedItems.signatureInfoStoreKeeper,
            assignedItems.signatureInfoSalesPerson
        )
        Log.e("111111111111", gh.toString())


        val single = postAssignedStockUseCase.getSingle(
            PostAssignedItems(
                assignedItems.storekeeperName,
                "",
                userModel!!.companyId,
                dateFormatter.convertToCalendarFormatDate(now()),
                ASSIGNMENT_RECEIVE_MODELS.productsSelection.value!!.toMutableList()?.mapFromWith(assignedItemsMapper),
                assignedItems.salespersonName,
                userModel!!.uuid,
                warehouseModel!!.id!!,
                "assignment",
                assignedItems.signatureInfoStoreKeeper,
                assignedItems.signatureInfoSalesPerson
            )
        )
        subscribeSingle(
            single,
            onSuccess = {
                isLoadingVisible = false
                ASSIGNMENT_RECEIVE_MODELS.productsSelectionTotalNumber.postValue(0)
                AssignedItemDetails.clearAssignedItemDetails()
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

//    private fun bitmapToFile(bitmap:Bitmap): Uri {
//        // Get the context wrapper
//        val wrapper = ContextWrapper(Context)
//
//        // Initialize a new file instance to save bitmap object
//        var file = wrapper.getDir("Images",Context.MODE_PRIVATE)
//        file = File(file,"${UUID.randomUUID()}.jpg")
//
//        try{
//            // Compress the bitmap and save in jpg format
//            val stream:OutputStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
//            stream.flush()
//            stream.close()
//        }catch (e:IOException){
//            e.printStackTrace()
//        }
//
//        // Return the saved bitmap uri
//        return Uri.parse(file.absolutePath)
//    }


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