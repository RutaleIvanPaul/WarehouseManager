package io.ramani.ramaniWarehouse.app.assignstock.flow

import io.ramani.ramaniWarehouse.app.AssignedStock.presentation.assignmentreceipt.AssignmentReceiptFragment
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonBottomSheetFragment
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.AssignSuccessFragment
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.AssignedStockSignaturePadFragment
import io.ramani.ramaniWarehouse.app.auth.presentation.SigninBottomSheetFragment
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ReturnStockSignaturePadFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ReturnSuccessFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.returnreceipt.ReturnReceiptFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonBottomSheetFragment

class AssignStockFlowcontroller(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : AssignStockFlow {
    override fun openAssignStockSalesPersonBottomSheet() {
        val fragment = AssignStockSalesPersonBottomSheetFragment()
        activity?.supportFragmentManager?.let { fragment.show(it, "salesperson_sheet_fragment") }
    }


    override fun openAssignStockSignPad(signee: String) {
        val fragment = AssignedStockSignaturePadFragment.newInstance(signee)
        activity?.supportFragmentManager?.let { fragment.show(it, "return_stock_signature_pad") }
    }

    override fun openAssignSuccess() {
        val fragment = AssignSuccessFragment.newInstance()
        activity?.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openAssignedStockPrintScreen() {
        val fragment = AssignmentReceiptFragment.newInstance()
        activity?.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.REPLACE
        )
    }
}