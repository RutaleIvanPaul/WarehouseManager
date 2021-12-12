package io.ramani.ramaniWarehouse.data.warehouses.mappers

import io.ramani.ramaniWarehouse.data.warehouses.models.InvoiceRemoteModel
import io.ramani.ramaniWarehouse.data.warehouses.models.ProductRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.warehouses.models.InvoiceModel
import io.ramani.ramaniWarehouse.domain.warehouses.models.ProductModel

class InvoiceRemoteModelMapper(private val productRemoteModelMapper: UniModelMapper<ProductRemoteModel,ProductModel>): UniModelMapper<InvoiceRemoteModel,InvoiceModel> {
    override fun mapFrom(from: InvoiceRemoteModel): InvoiceModel=
        InvoiceModel.Builder()
            .createdAt(from.createdAt)
            .distributorName(from.distributorName)
            .invoiceAmount(from.invoiceAmount)
            .invoiceId(from.invoiceId)
            .supplierName(from.supplierName)
            .products(from.products?.mapFromWith(productRemoteModelMapper))
            .build()
}