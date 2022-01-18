package io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow

import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveReceipt.ReceiveReceiptFragment
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveStock.ProductConfirmBottomSheetFragment
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.successReceive.ReceiveSuccessFragment
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.supplier.InvoiceBottomSheetFragment

class ReceiveStockFlowController(private val activity: BaseActivity) : ReceiveStockFlow {
    override fun openInvoiceFragment(purchaseId: String) {
        val fragment = InvoiceBottomSheetFragment.newInstance(purchaseId)
        activity?.supportFragmentManager?.let { fragment.show(it, "invoice_sheet_fragment") }
    }

    override fun openConfirmProductSheet(productId: String, onReceiveClicked: (String) -> Unit) {
        val fragment = ProductConfirmBottomSheetFragment(productId, onReceiveClicked)
        activity?.supportFragmentManager?.let {
            fragment.show(
                it,
                "product_confirm_sheet_fragment"
            )
        }
    }

    override fun openReceiveSuccess() {
        val fragment = ReceiveSuccessFragment.newInstance()
        activity?.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openReceiveReceipt() {
        val fragment = ReceiveReceiptFragment.newInstance()
        activity?.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.REPLACE
        )
    }
}