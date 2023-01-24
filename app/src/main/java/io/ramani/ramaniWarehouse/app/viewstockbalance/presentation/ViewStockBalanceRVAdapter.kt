package io.ramani.ramaniWarehouse.app.viewstockbalance.presentation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R

class ViewStockBalanceRVAdapter(
    data: MutableList<List<String>>
) :
    BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.item_view_stock_balance, data) {
    override fun convert(helper: BaseViewHolder, item: List<String>) {
        with(helper) {
            setText(R.id.item_view_stock_balance_product_name, item[0])
            setText(R.id.item_view_stock_balance_available_balance, item[2])
        }
    }
}