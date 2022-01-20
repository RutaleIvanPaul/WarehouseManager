package io.ramani.ramaniWarehouse.data.stockassignment.model

import android.content.Context
import android.graphics.Bitmap
import io.ramani.ramaniWarehouse.domain.base.v2.Params


data class PostAssignedItems(
    val assigner: String = "",
    val comment: String = "",
    val companyId: String = "",
    val dateStockTaken: String = "",
    val listOfProducts: List<ConfirmProducts> = listOf(),
    val name: String = "",
    val salesPersonUID: String = "",
    val warehouseId: String = "",
    val stockAssignmentType: String = "" ,
    var signatureInfoStoreKeeper: Bitmap?= null,
    var signatureInfoSalesPerson: Bitmap?= null,
    val context: Context? = null
):Params