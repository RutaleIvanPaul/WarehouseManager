package io.ramani.ramaniWarehouse.app.viewstockbalance.model

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.data.stockassignment.model.GetProductsRequestModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.model.*
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class ViewStockBalanceViewModel(application: Application,
                                stringProvider: IStringProvider,
                                sessionManager: ISessionManager,
                                private val getCompanyProductsUsecase: BaseSingleUseCase<List<ProductEntity>, GetProductsRequestModel>,
                                private val getReportsQueryUsecase: BaseSingleUseCase<ReportsQueryModel, ReportsQueryRequestModel>,
                                private val prefs: PrefsManager,
                                private val productUIModelMapper: ModelMapper<ProductEntity, ProductsUIModel>
                                ): BaseViewModel(application, stringProvider, sessionManager) {

    var companyId = ""
    var warehouseId = ""
    var warehouseName = ""
    var salesPersonUID = ""

    val loading = MutableLiveData<Boolean>()
    val noProducts = MutableLiveData<Boolean>()
    var stockBalanceModel = ReportsQueryModel()

    val stringName = MutableLiveData<String>()

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {

        sessionManager.getLoggedInUser().subscribeBy {
            salesPersonUID = it.uuid
            companyId = it.companyId
        }

        sessionManager.getCurrentWarehouse().subscribeBy {
            warehouseId = it.id ?: ""
            warehouseName = it.name ?: ""
        }
    }

    fun getCompanyProducts(){
        loading.postValue(true)
            val single = getCompanyProductsUsecase
                .getSingle(
                    //TO DO HERE
                    GetProductsRequestModel(companyId))
            subscribeSingle(
                single,
                onSuccess = {
                    loading.postValue(false)

                    isLoadingVisible = false

                    if(it.isNotEmpty()) {

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

        isLoadingVisible = true
        loading.postValue(true)

        val single = getReportsQueryUsecase.getSingle(requestParam)
        subscribeSingle(
            single,
            onSuccess = {
                stockBalanceModel = it
                //getCompanyProducts()

                isLoadingVisible = false
                loading.postValue(false)
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
            if (modelClass.isAssignableFrom(ViewStockBalanceViewModel::class.java)) {
                return ViewStockBalanceViewModel(
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