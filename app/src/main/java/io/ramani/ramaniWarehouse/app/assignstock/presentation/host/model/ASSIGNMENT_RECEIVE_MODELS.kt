package io.ramani.ramaniWarehouse.app.assignstock.presentation.host.model

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.CompanyProductsViewmodel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.domain.base.SingleLiveEvent

class ASSIGNMENT_RECEIVE_MODELS {
    companion object {
        fun resetAssignmentDetails(){
            val assignedItemsIDS = mutableListOf<String>()
            val productsSelection = MutableLiveData<List<ProductsUIModel>>()
            val productsSelectionTotalNumber = MutableLiveData<Int>()
            AssignStockViewModel.assignedItemDetails.signatureInfoSalesPerson = null
            AssignStockViewModel.assignedItemDetails.signatureInfoStoreKeeper = null
            AssignStockSalesPersonViewModel.selectedSalespersonLiveData.postValue(null)
//            AssignStockViewModel.signedLiveData.postValue(null)


        }
        var stockAssignModelView: AssignStockViewModel? = null
        var companyProductsViewModel: CompanyProductsViewmodel? = null
        val assignedItemsIDS = mutableListOf<String>()
        val refreshReceiveProductListLiveData = SingleLiveEvent<Boolean>()
        val productsSelection = MutableLiveData<List<ProductsUIModel>>()
        val productsSelectionTotalNumber = MutableLiveData<Int>()
        val storeSign = MutableLiveData<Bitmap>()
        val salesSign = MutableLiveData<Bitmap>()
       // val salesSign: Bitmap? = null
    }
}