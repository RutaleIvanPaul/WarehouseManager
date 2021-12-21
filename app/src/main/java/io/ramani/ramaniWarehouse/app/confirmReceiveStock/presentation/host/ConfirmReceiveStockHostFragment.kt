package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.host

import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.fragment_signin_sheet.loader
import kotlinx.android.synthetic.main.fragment_stock_receive_main.*
import kotlinx.android.synthetic.main.fragment_stock_receive_now_host.*
import org.kodein.di.generic.factory

private const val CREATED_AT_ARG = "created_at_arg"
private const val SUPPLIER_NAME_ARG = "supplier_name_arg"
private const val PURCHASE_ID_ARG = "purchase_id_arg"

class ConfirmReceiveStockHostFragment : BaseFragment() {

    companion object {
        fun newInstance(createdAt: String?, supplierName: String?, purchaseId: String?) =
            ConfirmReceiveStockHostFragment().apply {
                setArgs(
                    CREATED_AT_ARG to createdAt,
                    SUPPLIER_NAME_ARG to supplierName,
                    PURCHASE_ID_ARG to purchaseId
                )
            }
    }

    private val viewModelProvider: (Fragment) -> StockReceiveMainViewModel by factory()
    private lateinit var viewModel: StockReceiveMainViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow
    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_now_host
    private var createdAt: String? = ""
    private var supplierName: String? = ""
    private var purchaseId: String? = ""
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
        createdAt = arguments?.getString(CREATED_AT_ARG, "")
        supplierName = arguments?.getString(SUPPLIER_NAME_ARG, "")
        purchaseId = arguments?.getString(PURCHASE_ID_ARG, "")
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
        val adapter = TabPagerAdapter(activity)
        adapter.addFragment(SupplierConfirmReceiveFragment.newInstance(createdAt, supplierName, purchaseId), getString(R.string.supplier))
//        adapter.addFragment(
//            StockReceiveMainOthersFragment.newInstance(),
//            getString(R.string.others)
//        )

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