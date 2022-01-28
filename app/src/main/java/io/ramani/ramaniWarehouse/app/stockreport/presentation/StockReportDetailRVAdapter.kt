package io.ramani.ramaniWarehouse.app.stockreport.presentation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedItemModel

class StockReportDetailRVAdapter(
    data: MutableList<GoodsReceivedItemModel>
) :
    BaseQuickAdapter<GoodsReceivedItemModel, BaseViewHolder>(R.layout.item_stock_report_detail_item_row, data) {
    override fun convert(helper: BaseViewHolder, item: GoodsReceivedItemModel) {
        with(helper) {
            setText(R.id.stock_report_detail_item_row_name, item.productName)
            setText(R.id.stock_report_detail_item_row_quantity, String.format("%d %s", item.qtyAccepted, item.units))
        }
    }
}