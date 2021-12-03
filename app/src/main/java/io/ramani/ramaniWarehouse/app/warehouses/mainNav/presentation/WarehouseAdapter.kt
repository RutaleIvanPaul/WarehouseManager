package io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.model.WarehouseModelView

class WarehouseAdapter(
    data: MutableList<WarehouseModelView>,
    val onItemClick: (WarehouseModelView) -> Unit
) :
    BaseQuickAdapter<WarehouseModelView, BaseViewHolder>(R.layout.item_text, data) {
    override fun convert(helper: BaseViewHolder, item: WarehouseModelView) {
        with(helper) {
            setText(R.id.item_text_label, item.name)
            getView<TextView>(R.id.item_text_label).setTextColor(
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