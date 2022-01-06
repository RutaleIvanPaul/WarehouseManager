package io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.model

import android.graphics.Bitmap
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem

data class ReturnItemDetails(
    var storekeeperName:String = "",
    var salespersonName:String = "",
    var salespersonUuid:String = "",
    var returnItems:MutableList<AvailableProductItem> = mutableListOf(),
    var signatureInfoStoreKeeper: Bitmap?= null,
    var signatureInfoSalesPerson: Bitmap?= null
)
