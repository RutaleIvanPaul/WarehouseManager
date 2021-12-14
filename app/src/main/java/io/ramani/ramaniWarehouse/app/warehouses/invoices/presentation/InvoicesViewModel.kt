package io.ramani.ramaniWarehouse.app.warehouses.invoices.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView
import io.ramani.ramaniWarehouse.domain.auth.manager.ISessionManager
import io.ramani.ramaniWarehouse.domain.base.exceptions.ItemNotFoundException
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.entities.PagedList
import io.ramani.ramaniWarehouse.domain.warehouses.models.GetWarehousesRequestModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class InvoicesViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val loadInvoicesUseCase: BaseSingleUseCase<PagedList<InvoiceModel>, GetWarehousesRequestModel>,
    private val invoiceModelMapper: UniModelMapper<InvoiceModel, InvoiceModelView>

) : BaseViewModel(application, stringProvider, sessionManager) {
    val invoicesList = mutableListOf<InvoiceModelView>()
    private var page = 1
    var hasMoreToLoad = true
    val onInvoicesLoadedLiveData = MutableLiveData<Boolean>()

    override fun start(args: Map<String, Any?>) {
        loadInvoices()
    }

    fun loadInvoices() {
        if (hasMoreToLoad) {
            isLoadingVisible = true
            sessionManager.getLoggedInUser().subscribeBy {
                val single =
                    loadInvoicesUseCase.getSingle(GetWarehousesRequestModel(it.companyId, page))
                subscribeSingle(single, onSuccess = {
                    isLoadingVisible = false
                    if (it.data.isNullOrEmpty()) {
                        onInvoicesLoadedLiveData.postValue(false)
                    } else {
                        invoicesList.addAll(
                            it.data.mapFromWith(invoiceModelMapper)
                        )
                        hasMoreToLoad = it.paginationMeta.hasNext
                        onInvoicesLoadedLiveData.postValue(true)
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

    fun loadMore() {
        page++
        loadInvoices()
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val loadInvoicesUseCase: BaseSingleUseCase<PagedList<InvoiceModel>, GetWarehousesRequestModel>,
        private val invoiceModelMapper: UniModelMapper<InvoiceModel, InvoiceModelView>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InvoicesViewModel::class.java)) {
                return InvoicesViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    loadInvoicesUseCase,
                    invoiceModelMapper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}