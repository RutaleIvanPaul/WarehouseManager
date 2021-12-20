package io.ramani.ramaniWarehouse.app.returnstock.flow

import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ReturnStockSignaturePadFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ReturnSuccessFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonBottomSheetFragment

class ReturnStockFlowcontroller(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
): ReturnStockFlow {
    override fun openSalesPersonBottomSheet() {
        val fragment = SalesPersonBottomSheetFragment()
        activity?.supportFragmentManager?.let { fragment.show(it,"salesperson_sheet_fragment") }
    }

    override fun openReturnStockSignPad(signee: String) {
        val fragment = ReturnStockSignaturePadFragment.newInstance(signee)
        activity?.supportFragmentManager?.let { fragment.show(it, "return_stock_signature_pad") }
    }

    override fun openReturnSuccess() {
        val fragment = ReturnSuccessFragment.newInstance()
        activity?.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.REPLACE,
            addToBackStack = false
        )
    }
}