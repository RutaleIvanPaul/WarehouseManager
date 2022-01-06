package io.ramani.ramaniWarehouse.app.stockreceive.flow

interface StockReceiveFlow {
    fun openReceiveNow()
    fun openSignaturePad(what: String)
}