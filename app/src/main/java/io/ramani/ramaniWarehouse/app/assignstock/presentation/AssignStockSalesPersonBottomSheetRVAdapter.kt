package io.ramani.ramaniWarehouse.app.assignstock.presentation

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.model.SalesPersonRVModel
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener


class AssignStockSalesPersonBottomSheetRVAdapter(
    data: MutableList<SalesPersonRVModel>,
    val onItemClick: (SalesPersonRVModel) -> Unit
) :
    BaseQuickAdapter<SalesPersonRVModel, BaseViewHolder>(
        R.layout.salesperson_recyclerview_item,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: SalesPersonRVModel) {
        with(helper) {
            setText(R.id.salespersonname, item.name)
            getView<TextView>(R.id.salespersonname).setTextColor(
                if (item.isSelected == true) ContextCompat.getColor(
                    context,
                    R.color.light_green
                ) else ContextCompat.getColor(context, R.color.black)
            )
            helper.itemView.setOnSingleClickListener {
                onItemClick(item)
            }
        }
    }
}