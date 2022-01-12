package io.ramani.ramaniWarehouse.app.assignstock.presentation.products

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.data.stockassignment.model.GetProductsRequestModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider

class CompanyProductsViewmodel(application: Application,
                               stringProvider: IStringProvider,
                               sessionManager: ISessionManager,
                               private val getCompanyProductsUsecase: BaseSingleUseCase<List<ProductEntity>, GetProductsRequestModel>,
                               private val prefs: PrefsManager,
                               private val productUIModelMapper: ModelMapper<ProductEntity, ProductsUIModel>,

): BaseViewModel(application, stringProvider, sessionManager) {

    companion object{
        val noProductSelectedLiveData = MutableLiveData<Boolean>()
        val companyProductsListLiveData = MutableLiveData<ProductsUIModel>()
        val numberOfAssignedProductsLiveData = MutableLiveData<Int>()
    }

    val companyProductsListOriginal = mutableListOf<ProductsUIModel>()
    val companyProductsListLiveData = MutableLiveData<List<ProductsUIModel>>()
    val stringName = MutableLiveData<String>()

    override fun start(args: Map<String, Any?>) {
        TODO("Not yet implemented")

    }
    fun notifyLiveDataOfAssignmentChange(id: String, numberAssigned: Int){
        companyProductsListLiveData.map {
            it.forEach{
                it.isAssigned = it._id == id
                if(it.isAssigned!!) it.assignedNumber = numberAssigned
            }
        }
        numberOfAssignedProductsLiveData.postValue(numberOfAssignedProductsLiveData.value?.inc())
    }

    fun getCompanyProducts(){
            val single = getCompanyProductsUsecase
                .getSingle(
                    //TO DO HERE
                    GetProductsRequestModel("601ffa4d279d812ed25a7f9b"))
            subscribeSingle(
                single,
                onSuccess = {
                    isLoadingVisible = false

                    if(it.isNotEmpty()) {
                        numberOfAssignedProductsLiveData.postValue(0)
                        companyProductsListOriginal.addAll(it.mapFromWith(productUIModelMapper))
                        companyProductsListLiveData.postValue(companyProductsListOriginal.toMutableList())
                    }
                    else{
                        notifyError("Empty List",PresentationError.ERROR_TEXT)
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
        private val getCompanyProductsUsecase: BaseSingleUseCase<List<ProductEntity>, GetProductsRequestModel>,
        private val prefs: PrefsManager,
        private val productUIModelMapper: ModelMapper<ProductEntity, ProductsUIModel>,
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CompanyProductsViewmodel::class.java)) {
                return CompanyProductsViewmodel(
                    application,
                    stringProvider,
                    sessionManager,
                    getCompanyProductsUsecase,
                    prefs,
                    productUIModelMapper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }

    }
}