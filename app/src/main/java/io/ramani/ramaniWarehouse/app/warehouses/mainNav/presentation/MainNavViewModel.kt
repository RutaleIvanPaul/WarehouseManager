package io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.base.SingleLiveEvent
import io.ramani.ramaniWarehouse.domain.base.exceptions.ItemNotFoundException
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.mappers.mapToWith
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.entities.PagedList
import io.ramani.ramaniWarehouse.domain.warehouses.models.GetWarehousesRequestModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.WarehouseModel
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class MainNavViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val loadWarehousesUseCase: BaseSingleUseCase<PagedList<WarehouseModel>, GetWarehousesRequestModel>,
    private val prefs: PrefsManager,
    private val warehouseModelMapper: ModelMapper<WarehouseModel, WarehouseModelView>

) : BaseViewModel(application, stringProvider, sessionManager) {
    companion object {
        val warehousesList = mutableListOf<WarehouseModelView>()
        var page = 1
        var hasMoreToLoad = true
        var currentWarehouse: WarehouseModel? = null
        val onWarehousesLoadedLiveData = SingleLiveEvent<Boolean>()
        val onWarehousesSelectedLiveData = MutableLiveData<Pair<String?, Boolean>>()

        fun reset(){
            warehousesList.clear()
            page = 1
            hasMoreToLoad = true
            currentWarehouse=null
        }
    }


    override fun start(args: Map<String, Any?>) {

    }

    fun loadWarehouses() {
        if (hasMoreToLoad) {
            isLoadingVisible = true
            if(page == 1 ){
                warehousesList.clear()
            }
            sessionManager.getLoggedInUser().subscribeBy {
                val single =
                    loadWarehousesUseCase.getSingle(GetWarehousesRequestModel(it.companyId, page))
                subscribeSingle(single, onSuccess = {
                    isLoadingVisible = false
                    if (it.data.isNullOrEmpty()) {
                        onWarehousesLoadedLiveData.postValue(false)
                    } else {
                        warehousesList.addAll(
                            it.data.mapFromWith(warehouseModelMapper).toMutableList()
                        )
                        hasMoreToLoad = it.paginationMeta.hasNext
                        if (prefs.currentWarehouse.isNullOrBlank()) {
                            warehousesList.first().isSelected = true
                            prefs.currentWarehouse = it.data.first().toString()
                            currentWarehouse = it.data.first()
                        } else {
                            sessionManager.getCurrentWarehouse().subscribeBy { savedWarehouse ->
                                warehousesList.firstOrNull { it.id == savedWarehouse.id }?.isSelected =
                                    true
                                currentWarehouse = savedWarehouse
                            }
                        }
                        onWarehousesLoadedLiveData.postValue(true)
                    }
                }, onError = {
                    isLoadingVisible = false
                    if (it is ItemNotFoundException) {
                        hasMoreToLoad = false
                    } else {
                        notifyError(
                            it.message
                                ?: getString(R.string.an_error_has_occured_please_try_again),
                            PresentationError.ERROR_TEXT
                        )
                    }
                })
            }
        }
    }

    fun onWarehouseSelected(id: String, showCurrent: Boolean) {
        if (showCurrent) {
            warehousesList.map {
                it.isSelected = it.id == id
            }
            currentWarehouse =
                warehousesList.firstOrNull { it.id == id }?.mapToWith(warehouseModelMapper)
            if (currentWarehouse != null) {
                prefs.currentWarehouse = currentWarehouse.toString()
            }
//            onWarehousesSelectedLiveData.postValue(Pair(currentWarehouse?.name,true))
            onWarehousesLoadedLiveData.postValue(true)
        }else{
            onWarehousesSelectedLiveData.postValue(Pair(warehousesList.firstOrNull { it.id == id }?.name,true))
            AssignedItemDetails.assignedToWarehouseId = id
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val loadWarehousesUseCase: BaseSingleUseCase<PagedList<WarehouseModel>, GetWarehousesRequestModel>,
        private val prefs: PrefsManager,
        private val warehouseModelMapper: ModelMapper<WarehouseModel, WarehouseModelView>

    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainNavViewModel::class.java)) {
                return MainNavViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    loadWarehousesUseCase,
                    prefs,
                    warehouseModelMapper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}