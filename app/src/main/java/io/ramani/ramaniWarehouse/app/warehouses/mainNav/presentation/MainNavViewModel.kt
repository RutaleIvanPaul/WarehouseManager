package io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsManager
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
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
    private var page = 1
    private var hasMoreToLoad = false
    val warehousesList = mutableListOf<WarehouseModelView>()
    val warehousesItemsList = mutableListOf<String>()
    val onWarehousesLoadedLiveData = MutableLiveData<Boolean>()
    var currentWarehouse: WarehouseModel? = null
    override fun start(args: Map<String, Any?>) {
        loadWarehouses()
    }

    private fun loadWarehouses() {
        isLoadingVisible = true
        sessionManager.getLoggedInUser().subscribeBy {
            val single =
                loadWarehousesUseCase.getSingle(GetWarehousesRequestModel(it.companyId, page))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false
                if (it.data.isNullOrEmpty()) {
                    onWarehousesLoadedLiveData.postValue(false)
                } else {
                    warehousesList.addAll(it.data.mapFromWith(warehouseModelMapper).toMutableList())
                    hasMoreToLoad = it.paginationMeta.hasNext
                    if (hasMoreToLoad)
                        page++
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
                    warehousesList.forEach {
                        warehousesItemsList.add(it.name ?: "")
                    }
                    onWarehousesLoadedLiveData.postValue(true)
                }
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

    fun onWarehouseSelected(position: Int) {
        currentWarehouse = warehousesList[position].mapToWith(warehouseModelMapper)
        prefs.currentWarehouse = currentWarehouse.toString()
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val loadWarehousesUseCase: BaseSingleUseCase<PagedList<WarehouseModel>, GetWarehousesRequestModel>,
        private val prefs: PrefsManager,
        private val warehouseModelMapper: ModelMapper<WarehouseModel, WarehouseModelView>

    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
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