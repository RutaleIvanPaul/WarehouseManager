package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.host

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.code95.android.app.auth.flow.StockReceiveFlowController
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.adapters.TabPagerAdapter
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setArgs
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.supplier.SupplierConfirmReceiveFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.host.StockReceiveMainViewModel
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView
import kotlinx.android.synthetic.main.fragment_signin_sheet.loader
import kotlinx.android.synthetic.main.fragment_stock_receive_now_host.*
import org.kodein.di.generic.factory

private const val INVOICE_MODEL_VIEW_ARG = "invoice_model_view_arg"

class ConfirmReceiveStockHostFragment : BaseFragment() {

    companion object {
        fun newInstance(invoiceModelView: InvoiceModelView?) =
            ConfirmReceiveStockHostFragment().apply {
                setArgs(
                    INVOICE_MODEL_VIEW_ARG to invoiceModelView
                )
            }
    }

    private val viewModelProvider: (Fragment) -> StockReceiveMainViewModel by factory()
    private lateinit var viewModel: StockReceiveMainViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow
    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_now_host
    private var invoiceModelView: InvoiceModelView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        initArgs()
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        viewModel.start()
    }

    private fun initArgs() {
        invoiceModelView = arguments?.getParcelable(INVOICE_MODEL_VIEW_ARG)
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)
        initTabLayout()
    }

    private fun initTabLayout() {
        DrawableCompat.setTint(
            stock_receive_now_host_indicator_0.drawable,
            ContextCompat.getColor(requireContext(), R.color.ramani_green)
        )
        val adapter = TabPagerAdapter(activity)

        adapter.addFragment(
            SupplierConfirmReceiveFragment.newInstance(
                invoiceModelView?.createdAt,
                invoiceModelView?.supplierName,
                invoiceModelView?.purchaseOrderId
            ), getString(R.string.products)
        )

        stock_receive_now_host_viewpager.isUserInputEnabled = false
        stock_receive_now_host_viewpager.adapter = adapter
        stock_receive_now_host_viewpager.currentItem = 0
        TabLayoutMediator(
            stock_receive_now_host_tablayout,
            stock_receive_now_host_viewpager
        ) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
        stock_receive_now_host_tablayout.touchables.map { it.isClickable = false }
    }

}