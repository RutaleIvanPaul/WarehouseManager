package io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.model.SalespersonRVModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView

class SalespersonBottomSheetRVAdapter(
    data: MutableList<SalespersonRVModel>,
    val onItemClick: (SalespersonRVModel) -> Unit
) :
    BaseQuickAdapter<SalespersonRVModel, BaseViewHolder>(R.layout.salesperson_recyclerview_item, data) {
    override fun convert(helper: BaseViewHolder, item: SalespersonRVModel) {
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