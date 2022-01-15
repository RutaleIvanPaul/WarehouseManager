package io.ramani.ramaniWarehouse.app.confirmReceiveStock.model

import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView
import io.ramani.ramaniWarehouse.domain.base.SingleLiveEvent
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel

class RECEIVE_MODELS {
    companion object {
        fun reset() {
            invoiceModelView = null
            declineReasons = mutableListOf()
            goodsReceivedModel = GoodsReceivedModel()
            refreshReceiveProductListLiveData = SingleLiveEvent()
            refreshHostReceiveProductListLiveData = SingleLiveEvent()
        }

        var invoiceModelView: InvoiceModelView? = null
        var declineReasons = mutableListOf<String>()
        var goodsReceivedModel = GoodsReceivedModel()
        var refreshReceiveProductListLiveData = SingleLiveEvent<Boolean>()
        var refreshHostReceiveProductListLiveData = SingleLiveEvent<Boolean>()
    }
}