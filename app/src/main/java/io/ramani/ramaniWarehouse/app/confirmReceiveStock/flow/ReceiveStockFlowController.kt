package io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow

import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.supplier.InvoiceBottomSheetFragment

class ReceiveStockFlowController(private val activity: BaseActivity) : ReceiveStockFlow {
    override fun openInvoiceFragment(purchaseId: String) {
        val fragment = InvoiceBottomSheetFragment.newInstance(purchaseId)
        activity?.supportFragmentManager?.let { fragment.show(it, "invoice_sheet_fragment") }
    }
}