package io.ramani.ramaniWarehouse.app.stockreceive.flow

import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel

interface StockReceiveFlow {
    fun openReceiveNow()
    fun openSignaturePad(what: String)
    fun openReceiveSuccessPage(goodsReceivedModel: GoodsReceivedModel)
    fun openPrintPage(goodsReceivedModel: GoodsReceivedModel)
    fun openRootPage()
}