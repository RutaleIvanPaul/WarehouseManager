package io.ramani.ramaniWarehouse.app.warehouses.invoices.flow

import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView

interface InvoicesFlow {
    fun openConfirmReceiveStock(invoiceModelView: InvoiceModelView?)

}