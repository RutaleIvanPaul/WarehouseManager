package io.ramani.ramaniWarehouse.domain.stockreceive.model.selected

import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierProductModel

/**
 * @description     Selected product & properties
 *
 * @author          Adrian
 */
data class SelectedProductModel(
    var product: SupplierProductModel? = null,
    var productId: String = "",
    var productName: String = "",
    var units: String = "",
    var qtyAccepted: Int = 0,
    var qtyDeclined: Int = 0,
    var declinedReason: String = "",
    var unitPrice: Double = 0.0,
    var temperature: Int? = 0,
    var parameters: List<ProductParameterModel>? = null,
    var expireDate: String? = ""
)

data class ProductParameterModel(
    var name: String = "",
    var size: String = ""
)