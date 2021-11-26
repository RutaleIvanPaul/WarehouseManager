package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.code95.android.app.auth.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowViewModel.Companion.DATA_SUPPLIER
import io.ramani.ramaniWarehouse.domain.auth.model.SupplierProductModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import kotlinx.android.synthetic.main.fragment_stock_receive_products.*
import kotlinx.android.synthetic.main.fragment_stock_receive_supplier.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
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

    private val dateFormatter: DateFormatter by instance()
    private var availableProducts: ArrayList<SupplierProductModel> = ArrayList()

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

        products_why_declined_spinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            products_why_declined_spinner.isSelected = true
        }

        viewModel.start()
        subscribeObservers()
    }

    override fun onPause() {
        supplier_receiving_select_supplier_spinner.dismiss()

        super.onPause()
    }

    override fun onDestroy() {
        supplier_receiving_select_supplier_spinner.dismiss()

        super.onDestroy()
    }

    private fun subscribeObservers() {
        viewModel.validationResponseLiveData.observe(this, {

        })

        viewModel.getSuppliersActionLiveData.observe(this, {
            val items = ArrayList<String>()
            for (supplier in it)
                items.add(supplier.name)

            products_product_spinner.setItems(items)

        })

        viewModel.getDeclineReasonsActionLiveData.observe(this, {
            products_why_declined_spinner.setItems(it)
        })
    }

    fun updateView() {
        // Update available products
        availableProducts = viewModel.getAvailableProductsForSelectedSupplier() as ArrayList<SupplierProductModel>
        val items = ArrayList<String>()
        for (product in availableProducts)
            items.add(product.name)
        products_product_spinner.setItems(items)
    }

    fun doSave(): Boolean {
        val acceptedAmounts = products_accepted_amount.text.toString().toInt()
        val declinedAmouunts = products_declined_amount.text.toString().toInt()
        val unitPrice = products_unit_price.text.toString().toInt()
        val expireDate = products_expire_date.text.toString()

        return true
    }
}