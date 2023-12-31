package io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model

import android.content.Context
import android.graphics.Bitmap
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.model.ReturnItemDetails
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem
import io.ramani.ramaniWarehouse.data.stockassignment.model.ConfirmProducts
import java.io.File

object AssignedItemDetails {

    var storekeeperName: String = ""
    var salespersonName: String = ""
    var salespersonUuid: String = ""
    var assignedItems: MutableList<ProductsUIModel> = mutableListOf()
    var signatureInfoStoreKeeper: Bitmap? = null
    var signatureInfoSalesPerson: Bitmap? = null
    var signatureInfoStoreKeeperFile: File? = null
    var signatureInfoSalesPersonFile: File? = null
    val context: Context? = null

    fun clearAssignedItemDetails(){
        storekeeperName = ""
        ReturnItemDetails.salespersonName = ""
        ReturnItemDetails.salespersonUuid = ""
        ReturnItemDetails.returnItems = mutableListOf()
        ReturnItemDetails.signatureInfoStoreKeeper = null
        ReturnItemDetails.signatureInfoSalesPerson = null
    }
}
