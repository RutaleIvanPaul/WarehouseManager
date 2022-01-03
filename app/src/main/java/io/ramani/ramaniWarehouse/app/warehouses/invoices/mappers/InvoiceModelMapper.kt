package io.ramani.ramaniWarehouse.app.warehouses.invoices.mappers

import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel

class InvoiceModelMapper(private val dateFormatter: DateFormatter) :
    UniModelMapper<InvoiceModel, InvoiceModelView> {
    override fun mapFrom(from: InvoiceModel): InvoiceModelView =
        InvoiceModelView.Builder()
            .createdAt(dateFormatter.convertToDateWithDashes(from.createdAt ?: 0L))
            .distributorName(from.distributorName)
            .invoiceAmount(from.invoiceAmount)
            .invoiceId(from.invoiceId)
            .products(from.products)
            .supplierName(from.supplierName)
            .purchaseOrderId(from.purchaseOrderId)
            .build()
}