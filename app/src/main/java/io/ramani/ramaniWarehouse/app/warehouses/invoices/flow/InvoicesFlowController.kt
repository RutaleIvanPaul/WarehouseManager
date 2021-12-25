package io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow

import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.host.ConfirmReceiveStockHostFragment
import org.jetbrains.anko.AnkoLogger

class InvoicesFlowController(private val activity: BaseActivity) : InvoicesFlow, AnkoLogger {

    override fun openConfirmReceiveStock(
        createdAt: String?,
        supplierName: String?,
        purchaseOrderId: String?
    ) {
        val fragment = ConfirmReceiveStockHostFragment.newInstance(createdAt,supplierName,purchaseOrderId)
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }
}