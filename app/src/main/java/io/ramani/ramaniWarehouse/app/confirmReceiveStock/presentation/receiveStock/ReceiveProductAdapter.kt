package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveStock

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import org.jetbrains.anko.textColor

class ReceiveProductAdapter(
    data: MutableList<ProductModelView>,
    private val onReceiveClicked: (ProductModelView) -> Unit
) :
    BaseMultiItemQuickAdapter<ProductModelView, BaseViewHolder>(data) {
    init {
        addItemType(ProductModelView.TYPE.LABEL, R.layout.item_stock_confirm_receive_label)
        addItemType(ProductModelView.TYPE.PRODUCT, R.layout.item_stock_confirm_receive_product)
    }

    override fun convert(helper: BaseViewHolder, item: ProductModelView) {
        if (item.viewType == ProductModelView.TYPE.PRODUCT) {
            convertProduct(helper, item)
        }
    }

    private fun convertProduct(helper: BaseViewHolder, item: ProductModelView) {
        with(helper) {
            val productName = getView<TextView>(R.id.product_name)
            val productQty = getView<TextView>(R.id.product_qty)
            val productDeliveryStatus = getView<TextView>(R.id.product_delivery_status)
            val receivedStatusTv = getView<TextView>(R.id.received_status_tv)
            val receivedStatusIv = getView<ImageView>(R.id.received_status_iv)

            productName.text = item.productName
            productQty.text = String.format("%.0f", item.qtyPending)
            productDeliveryStatus.text = item.status

            item.status.let {
                var colorId: Int = R.color.transparent
                when (it) {
                    "In Progress" -> colorId = R.color.status_in_progress
                    "Complete" -> colorId = R.color.ramani_green
                    else -> colorId = R.color.status_pending
                }

                getView<ImageView>(R.id.product_delivery_status_iv).backgroundTintList = ContextCompat.getColorStateList(context, colorId)
            }

            if (item.isReceived == true) {
                receivedStatusTv.text = context.getString(R.string.received)
                receivedStatusTv.textColor = ContextCompat.getColor(context, R.color.ramani_green)
                receivedStatusIv.visibility = View.VISIBLE
            } else {
                receivedStatusTv.text = context.getString(R.string.receive)
                receivedStatusTv.textColor = ContextCompat.getColor(context, R.color.secondary_blue)
                receivedStatusIv.visibility = View.GONE
            }

            val canReceive = (item.qtyPending!! > 0)
            if (!canReceive)
                receivedStatusTv.textColor = ContextCompat.getColor(context, R.color.mid_dark_grey)

            getView<View>(R.id.product_receive_layout).isEnabled = canReceive
            getView<View>(R.id.product_receive_layout).setOnSingleClickListener {
                onReceiveClicked(item)
            }
        }
    }
}