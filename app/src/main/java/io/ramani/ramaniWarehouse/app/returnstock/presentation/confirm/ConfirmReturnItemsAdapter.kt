package io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem

class ConfirmReturnItemsAdapter(
    data: MutableList<AvailableProductItem>,
    val onItemClick: (AvailableProductItem) -> Unit
):BaseQuickAdapter<AvailableProductItem, BaseViewHolder>(R.layout.item_confirm_return_items,data) {
    override fun convert(holder: BaseViewHolder, item: AvailableProductItem) {
        with(holder){
            setText(R.id.item_confirm_return_product_name,item.productName)
            setText(R.id.item_confirm_return_product_quantity, item.quantity.toString())
        }
    }
}