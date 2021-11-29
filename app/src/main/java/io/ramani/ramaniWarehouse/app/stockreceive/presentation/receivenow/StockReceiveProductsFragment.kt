package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.code95.android.app.auth.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showConfirmDialog
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showErrorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowViewModel.Companion.DATA_SUPPLIER
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierProductModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import kotlinx.android.synthetic.main.fragment_stock_receive_now_host.*
import kotlinx.android.synthetic.main.fragment_stock_receive_products.*
import kotlinx.android.synthetic.main.fragment_stock_receive_supplier.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
    private var addedProducts: ArrayList<SelectedProductModel> = ArrayList()

    private val dateFormatter: DateFormatter by instance()
    private var calendar = Calendar.getInstance()

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

        // Add action
        products_add_product_button.setOnClickListener {
            if (doSave()) {
                updateProducts()
                clearAllFields()
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
            products_why_declined_spinner.setItems(it)
        })
    }

    private fun updateView() {
        // Update available products
        availableProducts = viewModel.getAvailableProductsForSelectedSupplier() as ArrayList<SupplierProductModel>

        updateProductSpinner()

        updateProducts()
    }

    fun doSave(): Boolean {
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
    }
}