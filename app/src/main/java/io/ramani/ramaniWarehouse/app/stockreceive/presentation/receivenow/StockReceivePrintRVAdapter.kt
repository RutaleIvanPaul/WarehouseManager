package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import java.util.*

class StockReceivePrintRVAdapter(
    data: MutableList<SelectedProductModel>
) :
    BaseQuickAdapter<SelectedProductModel, BaseViewHolder>(R.layout.item_stock_receive_print_item_row, data) {
    override fun convert(helper: BaseViewHolder, item: SelectedProductModel) {
        with(helper) {
            setText(R.id.stock_receive_print_item_row_name, item.product?.name ?: "")
            setText(R.id.stock_receive_print_item_row_accepted, String.format(Locale.getDefault(), "%d %s", item.qtyAccepted, item.units))
            setText(R.id.stock_receive_print_item_row_declined, String.format(Locale.getDefault(), "%d %s", item.qtyDeclined, item.units))
        }
    }
}