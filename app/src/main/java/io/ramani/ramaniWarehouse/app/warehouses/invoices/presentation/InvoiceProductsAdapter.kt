package io.ramani.ramaniWarehouse.app.warehouses.invoices.presentation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView

class InvoiceProductsAdapter(data: MutableList<ProductModelView>) :
    BaseQuickAdapter<ProductModelView, BaseViewHolder>(R.layout.item_invoice_product, data) {
    override fun convert(helper: BaseViewHolder, item: ProductModelView) {
        with(helper) {
            setText(R.id.invoice_product_tv, item.productName ?: "")
            setText(R.id.invoice_product_qty_tv, "${item.quantity ?: ""} ${item.units ?: ""}")
        }
    }
}