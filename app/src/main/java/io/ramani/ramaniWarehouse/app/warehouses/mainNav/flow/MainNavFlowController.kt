package io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow

import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.stockreport.presentation.StockReportFragment
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockFragment
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.model.ReturnItemDetails
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.host.StockReceiveMainFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation.StockStockAssignmentReportFragment
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.WarehouseBottomSheetFragment
import org.jetbrains.anko.AnkoLogger

class MainNavFlowController(private val activity: BaseActivity) : MainNavFlow, AnkoLogger {
    override fun openWarehousesBottomSheet(showCurrent:Boolean) {
        val fragment = WarehouseBottomSheetFragment(showCurrent)
        activity?.supportFragmentManager?.let { fragment.show(it, "warehouse_sheet_fragment") }

    }

    override fun openReceiveStock() {
        val fragment = StockReceiveMainFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openAssignStock() {
        AssignedItemDetails.clearAssignedItemDetails()
        AssignStockViewModel.signedLiveData.postValue(null)
        val fragment = AssignStockFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openReturnStock() {
        ReturnItemDetails.clearReturnItemDetails()
        ReturnStockViewModel.signedLiveData.postValue(null)
        val fragment = ReturnStockFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openStockReport() {
        val fragment = StockReportFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openStockAssignmentReport() {
        val fragment = StockStockAssignmentReportFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }
}