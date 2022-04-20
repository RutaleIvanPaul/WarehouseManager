package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveReceipt

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView

class ReceiptItemsAdapter(
    data: MutableList<ProductModelView>
) : BaseQuickAdapter<ProductModelView, BaseViewHolder>(R.layout.item_receipt_items, data) {
    override fun convert(holder: BaseViewHolder, item: ProductModelView) {
        with(holder) {
            setText(R.id.product_name, item.productName)
            setText(R.id.declined_tv, "${item.qtyDeclined} ${item.units}")
            setText(R.id.accepted_tv, "${item.qtyAccepted} ${item.units}")
        }
    }
}