package io.ramani.ramaniWarehouse.app.warehouses.invoices.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat.setTint
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.language.StringsPlaceHolders
import io.ramani.ramaniWarehouse.app.common.presentation.language.replacePlaceHolderWithText
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView
import io.ramani.ramaniWarehouse.data.common.prefs.PrefsConstants
import java.text.NumberFormat
import java.util.*

class InvoiceAdapter(
    data: MutableList<InvoiceModelView>,
    private val currency: String,
    private val onConfirmClicked: (InvoiceModelView) -> Unit
) :
    BaseQuickAdapter<InvoiceModelView, BaseViewHolder>(R.layout.item_invoice, data) {
    override fun convert(helper: BaseViewHolder, item: InvoiceModelView) {
        with(helper) {
            setText(R.id.date_tv, item.createdAt)
            setText(R.id.invoice_tv, item.invoiceId)
            setText(R.id.delivery_status_tv, item.invoiceStatus)

            item.invoiceStatus.let {
                var colorId: Int = R.color.transparent
                when (it) {
                    "In Progress" -> colorId = R.color.status_in_progress
                    "Complete" -> colorId = R.color.ramani_green
                    else -> colorId = R.color.status_pending
                }

                getView<ImageView>(R.id.delivery_status_iv).backgroundTintList = ContextCompat.getColorStateList(context, colorId)
            }

            setText(R.id.total_amount_tv, String.format("%s %s", currency, NumberFormat.getNumberInstance(Locale.US).format(item.invoiceAmount?.toInt())))
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