package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.confirmStock

import android.graphics.Bitmap
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import java.util.LinkedList

class SupportingDocumentAdapter(
    data: LinkedList<Bitmap>,
    val onItemClick:(Bitmap,Boolean) -> Unit
):BaseQuickAdapter<Bitmap,BaseViewHolder>(R.layout.supporting_document_item,data) {
    override fun convert(helper: BaseViewHolder, item: Bitmap) {
        with(helper){
            getView<TextView>(R.id.view_file).setOnSingleClickListener {
                onItemClick(item,false)
            }

            getView<View>(R.id.delete_file).setOnSingleClickListener {
                onItemClick(item,true)
            }
        }
    }
}