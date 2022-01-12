package io.ramani.ramaniWarehouse.app.warehouses.invoices.mappers

import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.ProductModel

class InvoiceModelMapper(
    private val dateFormatter: DateFormatter,
    private val productModelMapper: UniModelMapper<ProductModel, ProductModelView>
) :
    UniModelMapper<InvoiceModel, InvoiceModelView> {
    override fun mapFrom(from: InvoiceModel): InvoiceModelView =
        InvoiceModelView.Builder()
            .createdAt(dateFormatter.convertToDateWithDashes(from.createdAt ?: 0L))
            .distributorId(from.distributorId)
            .distributorName(from.distributorName)
            .invoiceAmount(from.invoiceAmount)
            .invoiceId(from.invoiceId)
            .products(from.products?.mapFromWith(productModelMapper))
            .supplierId(from.supplierId)
            .supplierName(from.supplierName)
            .purchaseOrderId(from.purchaseOrderId)
            .serverCreatedAtDateTime(from.serverCreatedAtDateTime)
            .build()
}