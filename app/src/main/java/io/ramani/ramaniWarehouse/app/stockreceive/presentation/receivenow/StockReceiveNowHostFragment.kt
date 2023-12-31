package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlowController
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.adapters.TabPagerAdapter
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showConfirmDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.tabs.StockReceiveConfirmFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.tabs.StockReceiveProductsFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.tabs.StockReceiveSupplierFragment
import kotlinx.android.synthetic.main.fragment_stock_receive_now_host.*
import org.kodein.di.generic.factory

class StockReceiveNowHostFragment : BaseFragment() {
    companion object {
        fun newInstance() = StockReceiveNowHostFragment()
    }

    private val viewModelProvider: (Fragment) -> StockReceiveNowViewModel by factory()
    private lateinit var viewModel: StockReceiveNowViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_now_host

    private var supplierFragment: StockReceiveSupplierFragment? = null
    private var productsFragment: StockReceiveProductsFragment? = null
    private var confirmFragment: StockReceiveConfirmFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        STOCK_RECEIVE_MODEL.clearData()
        initSubscribers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        stock_receive_now_host_back.setOnSingleClickListener {
            showConfirmDialog("Are you sure you want to cancel receive stocks?", onConfirmed = {
                STOCK_RECEIVE_MODEL.clearData()
                pop()
            })
        }

        // Next button handler
        stock_receive_now_host_next_button.setOnSingleClickListener {
            var allowGo = true

            val supplierData = STOCK_RECEIVE_MODEL.supplierData

            if (stock_receive_now_host_viewpager.currentItem == 0) {
                // Supplier page
                if (supplierData.supplier == null) {
                    errorDialog(getString(R.string.warning_select_supplier))
                    allowGo = false
                }

                if (allowGo) {
                    stock_receive_now_host_next_button.text = getString(R.string.next)
                    stock_receive_now_host_indicator_1.visibility = View.VISIBLE
                }

            }

            else if (stock_receive_now_host_viewpager.currentItem == 1) {
                // Product page
                if (supplierData.products.isNullOrEmpty()) {
                    errorDialog(getString(R.string.warning_add_product))
                    allowGo = false
                }

                if (allowGo) {
                    stock_receive_now_host_next_button.text = getString(R.string.done)
                    stock_receive_now_host_indicator_2.visibility = View.VISIBLE
                }
            }

            else if (stock_receive_now_host_viewpager.currentItem == 2) {
                // Confirm page
                if (supplierData.storeKeeperData == null) {
                    errorDialog(getString(R.string.warning_no_signed_store_keeper))
                    allowGo = false
                } else if (supplierData.deliveryPersonData == null) {
                    errorDialog(getString(R.string.warning_no_signed_delivery_person))
                    allowGo = false
                }

                // There is no need to go ahead.
                allowGo = false

                viewModel.postGoodsReceived(requireContext())
            }

            //    productsFragment?.updateView()

            if (allowGo)
                stock_receive_now_host_viewpager.currentItem += 1
        }

        initTabLayout()
    }

    private fun initSubscribers() {
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.validationResponseLiveData.observe(this, {

        })

        viewModel.getSuppliersActionLiveData.observe(this, {

        })

        viewModel.postGoodsReceivedActionLiveData.observe(this, {
            //STOCK_RECEIVE_MODEL.clearData()

            // Navigate to Success page
            flow.openReceiveSuccessPage(it)
        })

        STOCK_RECEIVE_MODEL.allowToGoNextLiveData.observe(this, {
            if (it.second) {
                when (it.first) {
                    0 -> DrawableCompat.setTint(stock_receive_now_host_indicator_0.drawable, ContextCompat.getColor(requireContext(), R.color.ramani_green));
                    1 -> DrawableCompat.setTint(stock_receive_now_host_indicator_1.drawable, ContextCompat.getColor(requireContext(), R.color.ramani_green));
                    2 -> DrawableCompat.setTint(stock_receive_now_host_indicator_2.drawable, ContextCompat.getColor(requireContext(), R.color.ramani_green));
                }
            }
        })

        STOCK_RECEIVE_MODEL.updateProductRequestLiveData.observe(this, {
            // If the request of updating product is posted, then go back to product page
            productsFragment?.requestProductUpdate(it)
            stock_receive_now_host_viewpager.setCurrentItem(1, true)
        })

        STOCK_RECEIVE_MODEL.updateProductCompletedLiveData.observe(this, {
            // If the request of updating product is completed, then go to confirm page
            confirmFragment?.updateView()
            stock_receive_now_host_viewpager.setCurrentItem(2, true)
        })

    }

    private fun initTabLayout() {
        supplierFragment = StockReceiveSupplierFragment.newInstance()
        productsFragment = StockReceiveProductsFragment.newInstance()
        confirmFragment = StockReceiveConfirmFragment.newInstance()

        val adapter = TabPagerAdapter(activity)
        adapter.addFragment(supplierFragment!!, getString(R.string.supplier))
        adapter.addFragment(productsFragment!!, getString(R.string.products))
        adapter.addFragment(confirmFragment!!, getString(R.string.confirm))

        stock_receive_now_host_viewpager.isUserInputEnabled = false
        stock_receive_now_host_viewpager.adapter = adapter
        stock_receive_now_host_viewpager.currentItem = 0
        TabLayoutMediator(stock_receive_now_host_tablayout, stock_receive_now_host_viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

        stock_receive_now_host_tablayout.touchables.forEach { it.isClickable = false }
    }

    override fun onBackButtonPressed(): Boolean {
        showConfirmDialog("Are you sure you want to cancel receive stocks?", onConfirmed = {
            STOCK_RECEIVE_MODEL.clearData()
            pop()
        })

        return true
    }
}