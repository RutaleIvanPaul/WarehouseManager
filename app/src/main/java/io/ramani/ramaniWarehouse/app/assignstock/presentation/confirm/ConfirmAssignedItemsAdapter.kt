package io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem

class ConfirmAssignedItemsAdapter(
    data: MutableList<ProductsUIModel>?,
    val onItemClick: (ProductsUIModel) -> Unit
):BaseQuickAdapter<ProductsUIModel, BaseViewHolder>(R.layout.item_confirm_assigned_products,data) {
    override fun convert(holder: BaseViewHolder, item: ProductsUIModel) {
        Log.e("cccccc", item.name)
        Log.e("cccccc", "item.name")
        with(holder){
            setText(R.id.item_confirm_assigned_product_name,item.name)
            setText(R.id.item_confirm_assigned_product_quantity, "${item.assignedNumber} PC")
        }
    }
}