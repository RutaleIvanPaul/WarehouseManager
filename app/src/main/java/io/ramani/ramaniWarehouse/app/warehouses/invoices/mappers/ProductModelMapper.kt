package io.ramani.ramaniWarehouse.app.warehouses.invoices.mappers

import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import io.ramani.ramaniWarehouse.domain.base.mappers.UniModelMapper
import io.ramani.ramaniWarehouse.domain.warehouses.models.ProductModel

class ProductModelMapper : UniModelMapper<ProductModel, ProductModelView> {
    override fun mapFrom(from: ProductModel): ProductModelView =
        ProductModelView.Builder()
            .productId(from.productId)
            .productName(from.productName)
            .price(from.price)
            .quantity(from.quantity)
            .unit(from.unit)
            .quantityAccepted(0.0)
            .quantityDeclined(0.0)
            .declineReason("")
            .isReceived(false)
            .price(0.0)
            .temp("")
            .status(from.status)
            .quantityPending(from.quantityPending)
            .quantityPendingBackup(from.quantityPending)
            .quantityAcceptedBackup(from.quantityAccepted)
            .quantityDeclinedBackup(from.quantityDeclined)
            .build()
}