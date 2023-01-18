package io.ramani.ramaniWarehouse.app.assignstock.presentation.products

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.model.ASSIGNMENT_RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.data.stockassignment.model.GetProductsRequestModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.ReportsQueryRemoteModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.model.*
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class CompanyProductsViewmodel(application: Application,
                               stringProvider: IStringProvider,
                               sessionManager: ISessionManager,
                               private val getCompanyProductsUsecase: BaseSingleUseCase<List<ProductEntity>, GetProductsRequestModel>,
                               private val getReportsQueryUsecase: BaseSingleUseCase<ReportsQueryModel, ReportsQueryRequestModel>,
                               private val prefs: PrefsManager,
                               private val productUIModelMapper: ModelMapper<ProductEntity, ProductsUIModel>,

): BaseViewModel(application, stringProvider, sessionManager) {

    var companyId = ""
    var warehouseId = ""
    var warehouseName = ""
    var salesPersonUID = ""
    var assignmentCOunter = 0

    companion object{
        val noProductSelectedLiveData = MutableLiveData<Boolean>()
        val companyProductsListLiveData = MutableLiveData<ProductsUIModel>()

    }

    val companyProductsListOriginal = mutableListOf<ProductsUIModel>()
    val companyProductsListSelection = mutableListOf<ProductsUIModel>()
    val companyProductsListLiveData = MutableLiveData<List<ProductsUIModel>>()
    val assignedCompanyProductsListLiveData = MutableLiveData<List<ProductsUIModel>>()
    val numberOfAssignedProductsLiveData = MutableLiveData<Int>()
    val serverProductsLoaded = MutableLiveData<Boolean>()
    val startLoadingProducts = MutableLiveData<Boolean>()
    val noProducts = MutableLiveData<Boolean>()
    var stockBalanceModel = ReportsQueryModel()

    val stringName = MutableLiveData<String>()

    override fun start(args: Map<String, Any?>) {

        sessionManager.getLoggedInUser().subscribeBy {
            salesPersonUID = it.uuid
            companyId = it.companyId
        }

        sessionManager.getCurrentWarehouse().subscribeBy {
            warehouseId = it.id ?: ""
            warehouseName = it.name ?: ""
        }

        serverProductsLoaded.postValue(false)
    }

    fun notifyLiveDataOfAssignmentChange(item: ProductsUIModel){
        if(!ASSIGNMENT_RECEIVE_MODELS.assignedItemsIDS.contains(item._id)) numberOfAssignedProductsLiveData.postValue(assignmentCOunter?.inc())
        ASSIGNMENT_RECEIVE_MODELS.assignedItemsIDS.add(item._id)
        ASSIGNMENT_RECEIVE_MODELS.productsSelectionTotalNumber.postValue(numberOfAssignedProductsLiveData.value)
        if(numberOfAssignedProductsLiveData.value != ASSIGNMENT_RECEIVE_MODELS.assignedItemsIDS.size){
            numberOfAssignedProductsLiveData.postValue(ASSIGNMENT_RECEIVE_MODELS.assignedItemsIDS.size)
        }
        numberOfAssignedProductsLiveData.postValue(ASSIGNMENT_RECEIVE_MODELS.assignedItemsIDS.distinct().size)
    }

    fun saveAllAssignedProducts(selection: List<ProductsUIModel>){
        companyProductsListSelection.addAll(selection)
        assignedCompanyProductsListLiveData.postValue(companyProductsListSelection.toMutableList())
        ASSIGNMENT_RECEIVE_MODELS.productsSelection.postValue(selection)
        ASSIGNMENT_RECEIVE_MODELS.assignedItemsIDS.clear()
    }

    fun resetViewModelData(){
        companyProductsListSelection.clear()
        assignedCompanyProductsListLiveData.postValue(emptyList())
        ASSIGNMENT_RECEIVE_MODELS.productsSelection.postValue(emptyList())
        assignedCompanyProductsListLiveData.postValue(emptyList())
    }

    fun getCompanyProducts(){
        startLoadingProducts.postValue(true)
            val single = getCompanyProductsUsecase
                .getSingle(
                    //TO DO HERE
                    GetProductsRequestModel(companyId))
            subscribeSingle(
                single,
                onSuccess = {
                    startLoadingProducts.postValue(false)
                    serverProductsLoaded.postValue(true)
                    numberOfAssignedProductsLiveData.postValue(0)

                    isLoadingVisible = false

                    if(it.isNotEmpty()) {
                        numberOfAssignedProductsLiveData.postValue(0)
                        companyProductsListOriginal.addAll(it.mapFromWith(productUIModelMapper))
                        companyProductsListLiveData.postValue(companyProductsListOriginal.toMutableList().distinct())
                    }
                    else{
                        noProducts.postValue(true)
                        notifyError("Empty List",PresentationError.ERROR_TEXT)
                    }
                }, onError = {
                    isLoadingVisible = false
                    notifyError(it.message
                        ?: getString(R.string.an_error_has_occured_please_try_again), PresentationError.ERROR_TEXT)
                }
            )
    }

    // Get balance of stocks from warehouse
    fun getReportsQuery(date: String){
        // Create request param
        val requestParam = createReportQueryRequestParam(date)

        startLoadingProducts.postValue(true)

        val single = getReportsQueryUsecase.getSingle(requestParam)
        subscribeSingle(
            single,
            onSuccess = {
                stockBalanceModel = it
                getCompanyProducts()
            }, onError = {
                isLoadingVisible = false

                getCompanyProducts()
            }
        )
    }

    private fun createReportQueryRequestParam(date: String): ReportsQueryRequestModel {
        val filter = ReportsQueryFilter()
        filter.value = warehouseId
        filter.label = warehouseName

        val query = ReportsQuery()
        query.startDate = date // "2023-01-16T07:07:49.830Z"
        query.endDate = date
        query.companyId = companyId
        query.userId = salesPersonUID
        query.filter = listOf(filter)

        val request = ReportsQueryRequestModel()
        request.queries = listOf(query)

        return request
    }

    fun getBalance(productId: String): Int {
        var balance = -1

        for (row in stockBalanceModel.rows) {
            // Assume that sixth element is id, and third element is balance in the warehouse
            if (row[5] == productId) {
                balance = if (TextUtils.isEmpty(row[2])) -1 else row[2].toInt()
                break
            }
        }

        return balance
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getCompanyProductsUsecase: BaseSingleUseCase<List<ProductEntity>, GetProductsRequestModel>,
        private val getReportsQueryUsecase: BaseSingleUseCase<ReportsQueryModel, ReportsQueryRequestModel>,
        private val prefs: PrefsManager,
        private val productUIModelMapper: ModelMapper<ProductEntity, ProductsUIModel>,
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CompanyProductsViewmodel::class.java)) {
                return CompanyProductsViewmodel(
                    application,
                    stringProvider,
                    sessionManager,
                    getCompanyProductsUsecase,
                    getReportsQueryUsecase,
                    prefs,
                    productUIModelMapper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }

    }
}