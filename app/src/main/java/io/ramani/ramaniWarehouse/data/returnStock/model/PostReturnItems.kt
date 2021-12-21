package io.ramani.ramaniWarehouse.data.returnStock.model

import android.graphics.Bitmap
import io.ramani.ramaniWarehouse.domain.base.v2.Params


data class PostReturnItems(
    val assigner: String = "",
    val comment: String = "",
    val companyId: String = "",
    val dateStockTaken: String = "",
    val listOfProducts: List<OfProducts> = listOf(),
    val name: String = "",
    val salesPersonUID: String = "",
    val warehouseId: String = "",
    val stockAssignmentType: String = "" ,
    var signatureInfoStoreKeeper: Bitmap?= null,
    var signatureInfoSalesPerson: Bitmap?= null
):Params