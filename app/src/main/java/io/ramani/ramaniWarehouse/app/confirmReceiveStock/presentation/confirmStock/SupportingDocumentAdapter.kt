package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.confirmStock

import android.graphics.Bitmap
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener

class SupportingDocumentAdapter(
    data: MutableList<String>,
    val onItemClick:(String) -> Unit
):BaseQuickAdapter<String,BaseViewHolder>(R.layout.supporting_document_item,data) {
    override fun convert(helper: BaseViewHolder, item: String) {
        with(helper){
            getView<TextView>(R.id.view_file).setOnSingleClickListener {
                onItemClick(item)
            }
        }
    }
}