package io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow

import io.ramani.ramaniWarehouse.app.auth.presentation.SigninBottomSheetFragment

interface MainNavFlow {
    fun openWarehousesBottomSheet()
    fun openReceiveStock()
    fun openAssignStock()
    fun openReturnStock()
    fun openStockReport()
    fun openStockAssignmentReport()
}