package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.tabs

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel

class StockReceiveConfirmProductRVAdapter(
    data: MutableList<SelectedProductModel>,
    val onItemClick: (SelectedProductModel) -> Unit
) :
    BaseQuickAdapter<SelectedProductModel, BaseViewHolder>(R.layout.item_product_confirm_row, data) {
    override fun convert(helper: BaseViewHolder, item: SelectedProductModel) {
        with(helper) {
            setText(R.id.item_product_confirm_row_product_name, item.product?.name ?: "")
            setText(R.id.item_product_confirm_row_agreed_amount, item.accepted.toString())
            setText(R.id.item_product_confirm_row_declined_amount, item.declined.toString())

            getView<TextView>(R.id.item_product_confirm_row_edit_action).setOnSingleClickListener {
                onItemClick(item)
            }
        }
    }
}