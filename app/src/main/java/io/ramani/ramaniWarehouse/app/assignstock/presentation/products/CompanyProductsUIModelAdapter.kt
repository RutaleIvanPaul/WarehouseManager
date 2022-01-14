package io.ramani.ramaniWarehouse.app.assignstock.presentation.products

import android.app.Dialog
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showErrorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import android.widget.LinearLayout.LayoutParams
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable


class CompanyProductsUIModelAdapter(
    data: MutableList<ProductsUIModel>,
    val onItemClick: (ProductsUIModel) -> Unit
) :
    BaseQuickAdapter<ProductsUIModel, BaseViewHolder>(R.layout.item_company_product, data) {
    override fun convert(helper: BaseViewHolder, item: ProductsUIModel) {
        with(helper) {
            Log.e("11111", item.isAssigned.toString())
            Log.e("11111", "item.isAssigned.toString()")
            setText(R.id.product_name, item.name)
            setText(R.id.product_description, item.supplierName)
            setText(R.id.product_assigned_number, "${item.assignedNumber.toString()} Assigned")
            getView<Button>(R.id.product_assign_button).setOnSingleClickListener {
                onItemClick(item)

            }
            helper.getView<ImageView>(R.id.product_image).apply {
                loadImage(item.imagePath)
            }

//            }.setOnClickListener(View.OnClickListener { showDialog(item) })

//            helper.itemView.setOnSingleClickListener {
//
//            }
        }
    }

    private fun showDialog(item: ProductsUIModel) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_assign_products)
        val body = dialog.findViewById(R.id.layout_custom_rewards_dialogue_box_product_id) as TextView
        val primaryUnits = dialog.findViewById(R.id.dialogue_custom_forms_custom_price_button) as TextView
        val secondaryUnits = dialog.findViewById(R.id.dialogue_custom_forms_qty_button) as TextView
        val productImage = dialog.findViewById(R.id.price_img) as ImageView
        val assignProductButton = dialog.findViewById(R.id.custom_products_assign_button) as Button

        body.text = item.name
        primaryUnits.text = item.units
        if (!item.hasSecondaryUnitConversion){
            primaryUnits.layoutParams.width = 600
            secondaryUnits.visibility = View.GONE

        }
        else {
            secondaryUnits.setOnClickListener(View.OnClickListener {
                secondaryUnits.background = getDrawable(context, R.drawable.round_white_outline_with_no_borders)
                secondaryUnits.text = item.secondaryUnitName
                primaryUnits.background = getDrawable(context, R.drawable.round_grey_outline_edit)
            })

            primaryUnits.setOnClickListener(View.OnClickListener {
                primaryUnits.background = getDrawable(context, R.drawable.round_white_outline_with_no_borders)
                secondaryUnits.background = getDrawable(context, R.drawable.round_grey_outline_edit)
            })
        }
        productImage.apply { loadImage(item.imagePath) }
        assignProductButton.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()

    }

}