package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.confirmStock

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView

class ConfirmedProductAdapter(
    data: MutableList<ProductModelView>
) :
    BaseMultiItemQuickAdapter<ProductModelView, BaseViewHolder>(data) {
    init {
        addItemType(ProductModelView.TYPE.LABEL, R.layout.item_stock_receive_confirm)
        addItemType(ProductModelView.TYPE.PRODUCT, R.layout.item_stock_receive_confirm)
    }

    override fun convert(helper: BaseViewHolder, item: ProductModelView) {
        if (item.viewType == ProductModelView.TYPE.LABEL) {
            convertLabel(helper, item)
        } else {
            convertProduct(helper, item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun convertLabel(helper: BaseViewHolder, item: ProductModelView) {
        with(helper) {
            val productName = getView<TextView>(R.id.product_name)
            val qtyAccepted = getView<TextView>(R.id.qty_accepted)
            val qtyReturned = getView<TextView>(R.id.qty_returned)
            val qtyPending = getView<TextView>(R.id.qty_pending)

            productName.setTypeface(null, Typeface.BOLD)
            qtyAccepted.setTypeface(null, Typeface.BOLD)
            qtyReturned.setTypeface(null, Typeface.BOLD)
            qtyPending.setTypeface(null, Typeface.BOLD)

            productName.text = "Item name"
            qtyAccepted.text = "Qty\nAccepted"
            qtyReturned.text = "Qty\nReturned"
            qtyPending.text = "Qty\nPending"

            setGone(R.id.separator, true)
        }
    }

    private fun convertProduct(helper: BaseViewHolder, item: ProductModelView) {
        with(helper) {
            val productName = getView<TextView>(R.id.product_name)
            val qtyAccepted = getView<TextView>(R.id.qty_accepted)
            val qtyReturned = getView<TextView>(R.id.qty_returned)
            val qtyPending = getView<TextView>(R.id.qty_pending)

            productName.setTypeface(null, Typeface.NORMAL)
            qtyAccepted.setTypeface(null, Typeface.NORMAL)
            qtyReturned.setTypeface(null, Typeface.NORMAL)
            qtyPending.setTypeface(null, Typeface.NORMAL)

            val pending = item.quantity!! - (item.qtyAccepted!! + item.qtyDeclined!!)
            productName.text = item.productName
            qtyAccepted.text = String.format("%.0f %s", item.qtyAccepted, item.units)
            qtyReturned.text = String.format("%.0f %s", item.qtyDeclined, item.units)
            qtyPending.text = String.format("%.0f %s", pending, item.units)
        }
    }
}