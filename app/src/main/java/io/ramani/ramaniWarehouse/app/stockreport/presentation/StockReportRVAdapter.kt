package io.ramani.ramaniWarehouse.app.stockreport.presentation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel

class StockReportRVAdapter(
    data: MutableList<DistributorDateModel>,
    val onItemClick: (DistributorDateModel) -> Unit
) :
    BaseQuickAdapter<DistributorDateModel, BaseViewHolder>(R.layout.item_stock_report_row, data) {
    override fun convert(helper: BaseViewHolder, item: DistributorDateModel) {
        with(helper) {
            setText(R.id.item_stock_report_row_name, item.deliveryPersonName)
            setText(R.id.item_stock_report_row_time, item.time)

            helper.itemView.setOnSingleClickListener {
                onItemClick(item)
            }
        }
    }
}