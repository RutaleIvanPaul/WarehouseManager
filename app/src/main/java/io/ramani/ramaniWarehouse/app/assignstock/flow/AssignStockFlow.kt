package io.ramani.ramaniWarehouse.app.assignstock.flow

interface AssignStockFlow {
    fun openAssignStockSalesPersonBottomSheet()
    fun openAssignStockSignPad(signee: String)
    fun openAssignSuccess()
    fun openAssignedStockPrintScreen()

}