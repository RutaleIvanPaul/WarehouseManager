package io.ramani.ramaniWarehouse.app.confirmReceiveStock.model

import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView
import io.ramani.ramaniWarehouse.domain.base.SingleLiveEvent
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel

class RECEIVE_MODELS {
    companion object {
        var invoiceModelView: InvoiceModelView? = null
        val declineReasons = mutableListOf<String>()
        val goodsReceivedModel = GoodsReceivedModel()
        val refreshReceiveProductListLiveData = SingleLiveEvent<Boolean>()
        val refreshHostReceiveProductListLiveData = SingleLiveEvent<Boolean>()
    }
}