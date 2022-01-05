package io.ramani.ramaniWarehouse.app.returnstock.presentation.products

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showErrorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.errors.PresentationError
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem

class ReturnItemsRVAdapter(
    data: MutableList<AvailableProductItem>,
    val onItemClick: (AvailableProductItem) -> Unit
) :
    BaseQuickAdapter<AvailableProductItem, BaseViewHolder>(R.layout.available_stock_return_item, data) {
    val returnItems = ReturnStockViewModel.returnItemDetails.returnItems
    override fun convert(helper: BaseViewHolder, item: AvailableProductItem) {
        with(helper) {
            val editQuantityEditText = getView<EditText>(R.id.return_stock_edit_quantity)
            val confirmQuantityButton = getView<Button>(R.id.return_stock_confirm_quantity_button)
            setText(R.id.return_product_name, item.productName)
            if(returnItems.filter { it.id == item.id }.isNotEmpty()){
                setText(R.id.return_stock_confirm_quantity_button,context.getString(R.string.Confirmed))
                setTextColor(R.id.return_stock_confirm_quantity_button,context.resources.getColor(R.color.light_green))
                editQuantityEditText.setText(item.quantity.toString())
            }

            confirmQuantityButton.setOnClickListener {

                if(editQuantityEditText.text.isNullOrEmpty()){
                    SelectReturnItemsViewmodel.missingValueLiveData.postValue(true)
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        returnItems.removeIf { itemFromList -> item.id == itemFromList.id }
                    } else {
                        returnItems.forEach {
                            if (it.id == item.id) {
                                remove(it)
                            }
                        }
                    }
                    item.quantity = editQuantityEditText.text.toString().toInt()

                    returnItems.add(item)
                    ReturnStockViewModel.returnItemsChangedLiveData.postValue(true)
                    setText(R.id.return_stock_confirm_quantity_button,context.getString(R.string.Confirmed))
                    setTextColor(R.id.return_stock_confirm_quantity_button,context.resources.getColor(R.color.light_green))
                }
            }

            editQuantityEditText.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
                if(hasFocus){
                    setBackgroundResource(R.id.return_stock_edit_quantity, R.drawable.green_stroke_action_button)
                }
                else{
                    setBackgroundResource(R.id.return_stock_edit_quantity, R.drawable.grey_stroke_next_action_button)
                }
            })

            editQuantityEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setText(R.id.return_stock_confirm_quantity_button,context.getString(R.string.confirm))
                    setTextColor(R.id.return_stock_confirm_quantity_button,context.resources.getColor(R.color.button_599BCB))
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
        }
    }

}