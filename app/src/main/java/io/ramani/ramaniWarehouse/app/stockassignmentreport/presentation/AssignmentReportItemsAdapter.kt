package io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.ProductReceivedItemModel

class AssignmentReportItemsAdapter(
    data: MutableList<ProductReceivedItemModel>,
    val onItemClick: (ProductReceivedItemModel) -> Unit
):BaseQuickAdapter<ProductReceivedItemModel, BaseViewHolder>(R.layout.item_confirm_return_items,data) {
    override fun convert(holder: BaseViewHolder, item: ProductReceivedItemModel) {
        with(holder){
            setText(R.id.item_confirm_return_product_name,item.productName)
            setText(R.id.item_confirm_return_product_quantity, "${item.quantity} ${item.units}")
        }
    }
}