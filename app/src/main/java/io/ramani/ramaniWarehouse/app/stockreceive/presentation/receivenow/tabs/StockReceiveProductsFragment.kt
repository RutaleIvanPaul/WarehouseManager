package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.tabs

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showConfirmDialog
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.SupplierProductModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.ProductParameterModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import kotlinx.android.synthetic.main.fragment_stock_receive_products.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

import android.widget.LinearLayout
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL.Companion.DATA_PRODUCTS
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowViewModel
import kotlinx.android.synthetic.main.item_stock_receive_product_parameter.view.*

class StockReceiveProductsFragment : BaseFragment() {
    companion object {
        fun newInstance() = StockReceiveProductsFragment()
    }

    private val viewModelProvider: (Fragment) -> StockReceiveNowViewModel by factory()
    private lateinit var viewModel: StockReceiveNowViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    // Products Variables
    private var availableProducts: ArrayList<SupplierProductModel> = ArrayList()
    private var declinedReasons: ArrayList<String> = ArrayList()

    private var addedProducts: ArrayList<SelectedProductModel> = ArrayList()
    private var parameters: ArrayList<ProductParameterModel> = ArrayList()

    private val dateFormatter: DateFormatter by instance()
    private var calendar = Calendar.getInstance()

    // Update Mode
    private var needToUpdateProduct = false
    private var updateNeedProduct: SelectedProductModel? = null

    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_products

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)

    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)

        // Initialize UI
        products_expire_date.text = dateFormatter.getCalendarTimeString(Date())

        products_product_spinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            updateUnitSpinner(availableProducts[newIndex])
        }

        products_why_declined_spinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            products_why_declined_spinner.isSelected = true
        }

        products_expire_date_layout.setOnClickListener {
            DatePickerDialog(requireActivity(),
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Reset fields
        products_reset_fields.setOnClickListener {
            showConfirmDialog("Are you sure you want to clear all fields?", onConfirmed = {
                clearAllFields()
            })
        }

        // Manage parameters
        product_add_parameter.setOnClickListener {
            addParametersLayout(ProductParameterModel("Temperature", ""))
        }
        addParametersLayout(ProductParameterModel("Temperature", ""))

        // Add action
        products_add_product_button.setOnClickListener {
            if (doSave()) {
                updateProducts()
                clearAllFields()

                STOCK_RECEIVE_MODEL.allowToGoNextLiveData.postValue(Pair(1, true))
            }
        }

        viewModel.getDeclineReasons()

        subscribeObservers()
        updateView()
    }

    override fun onPause() {
        products_product_spinner.dismiss()
        products_units_spinner.dismiss()
        products_why_declined_spinner.dismiss()

        super.onPause()
    }

    override fun onDestroy() {
        products_product_spinner.dismiss()
        products_units_spinner.dismiss()
        products_why_declined_spinner.dismiss()

        super.onDestroy()
    }

    private fun subscribeObservers() {
        viewModel.validationResponseLiveData.observe(this, {

        })

        viewModel.getDeclineReasonsActionLiveData.observe(this, {
            declinedReasons = it as ArrayList<String>
            products_why_declined_spinner.setItems(it)
        })

    }

    private fun updateView() {
        // Update available products
        availableProducts = viewModel.getAvailableProductsForSelectedSupplier() as ArrayList<SupplierProductModel>

        updateProductSpinner()

        updateProducts()
    }

    // Need to update product
    fun requestProductUpdate(product: SelectedProductModel) {
        updateNeedProduct = product

        product.product?.let {
            val prod = it

            needToUpdateProduct = true

            products_product_spinner.selectItemByIndex(availableProducts.indexOf(availableProducts.find { it.id == prod.id }))

            updateUnitSpinner(prod)
            products_units_spinner.selectItemByIndex(if (prod.units.equals(product.units)) 0 else 1)

            products_accepted_amount.setText(product.accepted.toString())
            products_declined_amount.setText(product.declined.toString())

            products_why_declined_spinner.selectItemByIndex(declinedReasons.indexOf(declinedReasons.find { it == product.declinedReason }))

            products_unit_price.setText(product.unitPrice.toString())
            products_expire_date.text = product.expireDate

        }
    }

    private fun doSave(): Boolean {
        if (products_product_spinner.selectedIndex < 0) {
            errorDialog(getString(R.string.warning_select_product))
            return false
        }

        if (products_units_spinner.selectedIndex < 0) {
            errorDialog(getString(R.string.warning_select_unit))
            return false
        }

        val acceptedAmounts = getFieldValueByInt(products_accepted_amount)
        val declinedAmouunts = getFieldValueByInt(products_declined_amount)
        if (acceptedAmounts < 0 && declinedAmouunts < 0) {
            errorDialog(getString(R.string.warning_put_amount))
            return false
        }

        if (declinedAmouunts > 0) {
            if (products_why_declined_spinner.selectedIndex < 0) {
                errorDialog(getString(R.string.warning_select_declined_reason))
                return false
            }
        }

        val unitPrice = getFieldValueByDouble(products_unit_price)
        if (unitPrice <= 0) {
            errorDialog(getString(R.string.warning_invalid_unit))
            return false
        }

        val expireDate = products_expire_date.text.toString()

        if (!needToUpdateProduct) {
            // Create one object
            val product = SelectedProductModel(
                availableProducts[products_product_spinner.selectedIndex],
                products_units_spinner.text.toString(),
                acceptedAmounts,
                declinedAmouunts,
                if (declinedAmouunts > 0) products_why_declined_spinner.text.toString() else "",
                unitPrice,
                null,
                expireDate
            )
            addedProducts.add(product)
        } else {
            updateNeedProduct?.product = availableProducts[products_product_spinner.selectedIndex]
            updateNeedProduct?.units = products_units_spinner.text.toString()
            updateNeedProduct?.accepted = acceptedAmounts
            updateNeedProduct?.declined = declinedAmouunts
            updateNeedProduct?.declinedReason = if (declinedAmouunts > 0) products_why_declined_spinner.text.toString() else ""
            updateNeedProduct?.unitPrice = unitPrice
            updateNeedProduct?.expireDate = expireDate

            needToUpdateProduct = false
            STOCK_RECEIVE_MODEL.updateProductCompletedLiveData.postValue(updateNeedProduct)

            // There is no need to update on UI
            return false;
        }

        return true
    }

    /**
     * Clear All Fields
     */
    private fun clearAllFields() {
        products_product_spinner.clearSelectedItem()

        updateUnitSpinner(SupplierProductModel())

        products_accepted_amount.setText("")
        products_declined_amount.setText("")
        products_unit_price.setText("")

        products_why_declined_spinner.clearSelectedItem()

        calendar = Calendar.getInstance()
        updateExpireDate()

        products_scroll_view.fullScroll(ScrollView.FOCUS_UP)

        products_product_spinner.clearFocus()
        products_units_spinner.clearFocus()
        products_accepted_amount.clearFocus()
        products_declined_amount.clearFocus()
        products_units_spinner.clearFocus()
        products_why_declined_spinner.clearFocus()
    }

    private fun updateProductSpinner() {
        val items = ArrayList<String>()
        for (product in availableProducts)
            items.add(product.name)
        products_product_spinner.setItems(items)
    }

    private fun updateUnitSpinner(product: SupplierProductModel) {
        val units = ArrayList<String>()
        if (!product.units.isBlank())
            units.add(product.units)
        if (!product.secondaryUnitName.isBlank())
            units.add(product.secondaryUnitName)

        products_units_spinner.setItems(units)

        products_units_spinner.clearSelectedItem()
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateExpireDate()
        }

    private fun updateExpireDate() {
        val myFormat = "MM-dd-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        products_expire_date.text = sdf.format(calendar.getTime())
    }

    private fun updateProducts() {
        products_added_amount.text = String.format("%d %s", addedProducts.size, getString(R.string.added))

        STOCK_RECEIVE_MODEL.setData(DATA_PRODUCTS, addedProducts)
    }

    /**
     * Add new parameters
     */
    private fun addParametersLayout(parameter: ProductParameterModel) {
        parameters.add(parameter)

        val listItem = ArrayList<String>()
        listItem.add(parameter.name)

        val itemView = LinearLayout.inflate(requireContext(), R.layout.item_stock_receive_product_parameter, null)
        itemView.products_parameter_spinner.setItems(listItem)
        itemView.products_parameter_size.setText(parameter.size)
        itemView.products_parameter_delete.setOnSingleClickListener {
            products_parameter_container.removeView(itemView)
        }

        products_parameter_container.addView(itemView)
    }
}