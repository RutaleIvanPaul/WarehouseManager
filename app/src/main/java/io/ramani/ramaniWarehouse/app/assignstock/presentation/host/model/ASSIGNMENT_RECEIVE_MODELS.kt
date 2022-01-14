package io.ramani.ramaniWarehouse.app.assignstock.presentation.host.model

import androidx.lifecycle.MutableLiveData
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.CompanyProductsViewmodel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.domain.base.SingleLiveEvent

class ASSIGNMENT_RECEIVE_MODELS {
    companion object {
        var stockAssignModelView: AssignStockViewModel? = null
        var companyProductsViewModel: CompanyProductsViewmodel? = null
        val declineReasons = mutableListOf<String>()
        val refreshReceiveProductListLiveData = SingleLiveEvent<Boolean>()
        val productsSelection = MutableLiveData<List<ProductsUIModel>>()
    }
}