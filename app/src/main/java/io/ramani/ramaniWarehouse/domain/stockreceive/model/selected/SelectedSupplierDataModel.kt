package io.ramani.ramaniWarehouse.domain.stockreceive.model.selected

import io.ramani.ramaniWarehouse.domain.auth.model.SupplierModel
import java.util.*

class SelectedSupplierDataModel {
    var supplier: SupplierModel? = null
    var date: Date? = null
    var documents: List<String>? = null

    var products: List<SelectedProductModel>? = null

    var storeKeeperData: SignatureInfo? = null
    var deliveryPersonData: SignatureInfo? = null

    fun clear() {
        supplier = null
        date = null

        documents = null
        products = null
        storeKeeperData = null
        deliveryPersonData = null
    }
}