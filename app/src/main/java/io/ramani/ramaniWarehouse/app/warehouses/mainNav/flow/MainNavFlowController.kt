package io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow

import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.WarehouseBottomSheetFragment
import org.jetbrains.anko.AnkoLogger

class MainNavFlowController(private val activity: BaseActivity) : MainNavFlow, AnkoLogger {
    override fun openWarehousesBottomSheet() {
        val fragment = WarehouseBottomSheetFragment()
        activity?.supportFragmentManager?.let { fragment.show(it, "warehouse_sheet_fragment") }

    }

    override fun openReceiveStock() {
        //TODO("Not yet implemented")
    }

    override fun openAssignStock() {
        //TODO("Not yet implemented")
    }

    override fun openReturnStock() {
        //TODO("Not yet implemented")
    }

    override fun openStockReport() {
        //TODO("Not yet implemented")
    }

    override fun openAssignmentReport() {
        //TODO("Not yet implemented")
    }
}