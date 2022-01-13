package io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow

interface ReceiveStockFlow {
    fun openInvoiceFragment(purchaseId: String)
    fun openConfirmProductSheet(productId:String,onReceiveClicked: (String) -> Unit)
    fun openReceiveSuccess()
}