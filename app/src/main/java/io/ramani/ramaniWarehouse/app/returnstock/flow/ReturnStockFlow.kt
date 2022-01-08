package io.ramani.ramaniWarehouse.app.returnstock.flow

interface ReturnStockFlow {
    fun openSalesPersonBottomSheet()
    fun openReturnStockSignPad(signee: String)
    fun openReturnSuccess()
}