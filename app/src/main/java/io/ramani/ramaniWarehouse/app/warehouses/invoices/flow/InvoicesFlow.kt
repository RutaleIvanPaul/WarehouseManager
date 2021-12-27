package io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow

interface InvoicesFlow {
    fun openConfirmReceiveStock(createdAt: String?, supplierName: String?, purchaseOrderId: String?)

}