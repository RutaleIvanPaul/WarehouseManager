package io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model

import android.graphics.Bitmap
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem
import io.ramani.ramaniWarehouse.data.stockassignment.model.ConfirmProducts

data class AssignedItemDetails(
    var storekeeperName:String = "",
    var salespersonName:String = "",
    var salespersonUuid:String = "",
    var assignedItems:MutableList<ProductsUIModel> = mutableListOf(),
    var signatureInfoStoreKeeper: Bitmap?= null,
    var signatureInfoSalesPerson: Bitmap?= null
)
