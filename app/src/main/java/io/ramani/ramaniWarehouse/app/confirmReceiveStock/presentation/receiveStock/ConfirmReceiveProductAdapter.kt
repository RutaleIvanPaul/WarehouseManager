package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveStock

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import org.jetbrains.anko.textColor

class ConfirmReceiveProductAdapter(
    data: MutableList<ProductModelView>,
    private val onReceiveClicked: (ProductModelView) -> Unit
) :
    BaseMultiItemQuickAdapter<ProductModelView, BaseViewHolder>(data) {
    init {
        addItemType(ProductModelView.TYPE.LABEL, R.layout.item_stock_confirm_receive_product)
        addItemType(ProductModelView.TYPE.PRODUCT, R.layout.item_stock_confirm_receive_product)
    }

    override fun convert(helper: BaseViewHolder, item: ProductModelView) {
        if (item.viewType == ProductModelView.TYPE.LABEL) {
            convertLabel(helper, item)
        } else {
            convertProduct(helper, item)
        }
    }

    private fun convertLabel(helper: BaseViewHolder, item: ProductModelView) {
        with(helper) {
            val productName = getView<TextView>(R.id.product_name)
            val productQty = getView<TextView>(R.id.product_qty)
            val productStatus = getView<TextView>(R.id.product_status)

            productName.setTypeface(null, Typeface.BOLD)
            productQty.setTypeface(null, Typeface.BOLD)
            productStatus.setTypeface(null, Typeface.BOLD)

            productName.text = ""
            productQty.text = item.productName
            productStatus.text = item.temp

            setGone(R.id.status_received_iv, true)
            setGone(R.id.separator, true)
        }
    }

    private fun convertProduct(helper: BaseViewHolder, item: ProductModelView) {
        with(helper) {
            val productName = getView<TextView>(R.id.product_name)
            val productQty = getView<TextView>(R.id.product_qty)
            val productStatus = getView<TextView>(R.id.product_status)

            productName.setTypeface(null, Typeface.NORMAL)
            productQty.setTypeface(null, Typeface.NORMAL)
            productStatus.setTypeface(null, Typeface.NORMAL)

            productName.text = item.productName
            productQty.text = item.quantity.toString()
            if (item.isReceived == true) {
                productStatus.text = context.getString(R.string.received)
                productStatus.textColor =
                    ContextCompat.getColor(context, R.color.text_green)
            } else {
                productStatus.text = context.getString(R.string.receive)
                productStatus.textColor =
                    ContextCompat.getColor(context, R.color.secondary_blue)
                setGone(R.id.status_received_iv, item.isReceived == false)
            }
            productStatus.setOnSingleClickListener {
                if (item.isReceived == false) {
                    onReceiveClicked(item)
                }
            }
        }
    }
}