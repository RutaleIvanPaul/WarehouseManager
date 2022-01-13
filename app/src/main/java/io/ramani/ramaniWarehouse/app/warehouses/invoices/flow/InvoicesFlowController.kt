package io.ramani.ramaniWarehouse.app.warehouses.invoices.flow

import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.host.ConfirmReceiveStockHostFragment
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView
import org.jetbrains.anko.AnkoLogger

class InvoicesFlowController(private val activity: BaseActivity) : InvoicesFlow, AnkoLogger {

    override fun openConfirmReceiveStock(
        invoiceModelView: InvoiceModelView?
    ) {
        val fragment = ConfirmReceiveStockHostFragment.newInstance(invoiceModelView)
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }
}