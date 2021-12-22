package io.ramani.ramaniWarehouse.app.assignstock.flow

import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonBottomSheetFragment
import io.ramani.ramaniWarehouse.app.auth.presentation.SigninBottomSheetFragment
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonBottomSheetFragment

class AssignStockFlowcontroller(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : AssignStockFlow {
    override fun openAssignStockSalesPersonBottomSheet() {
        val fragment = AssignStockSalesPersonBottomSheetFragment()
        activity?.supportFragmentManager?.let { fragment.show(it, "salesperson_sheet_fragment") }
    }
}