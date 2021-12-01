package io.ramani.ramaniWarehouse.app.returnstock.flow

import io.ramani.ramaniWarehouse.app.auth.presentation.SigninBottomSheetFragment
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonBottomSheetFragment

class ReturnStockFlowcontroller(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
): ReturnStockFlow {
    override fun openSalesPersonBottomSheet() {
        val fragment = SalesPersonBottomSheetFragment()
        activity?.supportFragmentManager?.let { fragment.show(it,"salesperson_sheet_fragment") }
    }
}