package io.ramani.ramaniWarehouse.app.warehouses.invoices.presentation

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.language.StringsPlaceHolders
import io.ramani.ramaniWarehouse.app.common.presentation.language.replacePlaceHolderWithText
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView

class InvoiceAdapter(
    data: MutableList<InvoiceModelView>,
    private val onConfirmClicked: (InvoiceModelView) -> Unit
) :
    BaseQuickAdapter<InvoiceModelView, BaseViewHolder>(R.layout.item_invoice, data) {
    override fun convert(helper: BaseViewHolder, item: InvoiceModelView) {
        with(helper) {
            setText(R.id.date_tv, item.createdAt)
            setText(R.id.invoice_tv, item.invoiceId)
            setText(R.id.total_amount_tv, String.format("%.2f", item.invoiceAmount))
            setText(
                R.id.order_from,
                getView<TextView>(R.id.order_from).text.toString().replacePlaceHolderWithText(
                    StringsPlaceHolders.warehouse,
                    item.distributorName ?: ""
                )
            )
//            val productsAdapter =
//                InvoiceProductsAdapter(item.products?.toMutableList() ?: mutableListOf())
//            getView<RecyclerView>(R.id.products_rv).apply {
//                adapter = productsAdapter
//            }

            getView<TextView>(R.id.accept_btn).setOnSingleClickListener {
                onConfirmClicked(item)
            }
        }
    }
}