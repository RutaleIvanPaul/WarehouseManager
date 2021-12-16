package io.ramani.ramaniWarehouse.app.returnstock.presentation.products

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem

class ReturnItemsRVAdapter(
    data: MutableList<AvailableProductItem>,
    val onItemClick: (AvailableProductItem) -> Unit
) :
    BaseQuickAdapter<AvailableProductItem, BaseViewHolder>(R.layout.available_stock_return_item, data) {
    override fun convert(helper: BaseViewHolder, item: AvailableProductItem) {
        with(helper) {
            setText(R.id.return_product_name, item.productName)

            getView<Button>(R.id.return_stock_confirm_quantity_button).setOnClickListener {
                setText(R.id.return_stock_confirm_quantity_button,context.getString(R.string.Confirmed))
                setTextColor(R.id.return_stock_confirm_quantity_button,context.resources.getColor(R.color.light_green))
                setBackgroundResource(R.id.return_stock_confirm_quantity_button, R.drawable.green_stroke_action_button)
            }

            getView<EditText>(R.id.return_stock_edit_quantity).setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
                if(hasFocus){
                    setBackgroundResource(R.id.return_stock_edit_quantity, R.drawable.green_stroke_action_button)
                }
                else{
                    setBackgroundResource(R.id.return_stock_edit_quantity, R.drawable.grey_stroke_next_action_button)
                }
            })
//            getView<TextView>(R.id.salespersonname).setTextColor(
//                if (item.isSelected == true) ContextCompat.getColor(
//                    context,
//                    R.color.light_green
//                ) else ContextCompat.getColor(context, R.color.black)
//            )
//            helper.itemView.setOnSingleClickListener {
//                onItemClick(item)
//            }
        }
    }
}