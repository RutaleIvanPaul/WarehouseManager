package io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow

import io.ramani.ramaniWarehouse.app.auth.presentation.SigninBottomSheetFragment

interface MainNavFlow {
    fun openWarehousesBottomSheet(showCurrent:Boolean)
    fun openReceiveStock()
    fun openAssignStock()
    fun openReturnStock()
    fun openViewStockBalance()
    fun openStockReport()
    fun openStockAssignmentReport()
}