package io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow

import io.ramani.ramaniWarehouse.app.assignmentreport.presentation.AssignmentReportFragment
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockFragment
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.model.ReturnItemDetails
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.host.StockReceiveMainFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockFragment
import io.ramani.ramaniWarehouse.app.stockStockAssignmentReport.presentation.StockStockAssignmentReportFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.app.warehouses.invoices.presentation.InvoicesFragment
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.WarehouseBottomSheetFragment
import org.jetbrains.anko.AnkoLogger

class MainNavFlowController(private val activity: BaseActivity) : MainNavFlow, AnkoLogger {
    override fun openWarehousesBottomSheet() {
        val fragment = WarehouseBottomSheetFragment()
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
        //TODO("Not yet implemented")
    }

    override fun openAssignmentReport() {
        val fragment = AssignmentReportFragment.newInstance()
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