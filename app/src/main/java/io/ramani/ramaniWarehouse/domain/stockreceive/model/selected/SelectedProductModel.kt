package io.ramani.ramaniWarehouse.domain.stockreceive.model.selected

import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierProductModel
import java.util.*

/**
 * @description     Selected product & properties
 *
 * @author          Adrian
 */
class SelectedProductModel {
    var product: SupplierProductModel? = null
    var units: String = ""
    var accepted: Int = 0
    var declined: Int = 0
    var declinedReason: String = ""
    var unitPrice: Int = 0
    var parameters: List<ProductParameterModel>? = null
    var expireDate: Date? = null
}

class ProductParameterModel {
    var name: String = ""
    var size: String = ""
}