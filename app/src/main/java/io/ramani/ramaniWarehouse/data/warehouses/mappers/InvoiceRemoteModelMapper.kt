package io.ramani.ramaniWarehouse.data.warehouses.mappers

import io.ramani.ramaniWarehouse.data.warehouses.models.InvoiceRemoteModel
import io.ramani.ramaniWarehouse.data.warehouses.models.ProductRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.ProductModel

class InvoiceRemoteModelMapper(
    private val productRemoteModelMapper: UniModelMapper<ProductRemoteModel, ProductModel>,
    private val dateFormatter: DateFormatter
) : UniModelMapper<InvoiceRemoteModel, InvoiceModel> {
    override fun mapFrom(from: InvoiceRemoteModel): InvoiceModel =
        InvoiceModel.Builder()
            .createdAt(dateFormatter.getDateInMillisFromServerDate(from.createdAt ?: ""))
            .distributorId(from.distributorId)
            .distributorName(from.distributorName)
            .invoiceAmount(from.invoiceAmount)
            .invoiceId(from.invoiceId)
            .supplierId(from.supplierId)
            .supplierName(from.supplierName)
            .products(from.products?.mapFromWith(productRemoteModelMapper))
            .purchaseOrderId(from.purchaseOrderId)
            .invoiceStatus(from.invoiceStatus)
            .serverCreatedAtDateTime(from.createdAt)
            .build()
}