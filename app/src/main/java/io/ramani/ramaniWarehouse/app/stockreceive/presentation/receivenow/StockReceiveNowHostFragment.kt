package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.code95.android.app.auth.flow.StockReceiveFlowController
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showConfirmDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import kotlinx.android.synthetic.main.fragment_stock_receive_now_host.*
import kotlinx.android.synthetic.main.fragment_stock_receive_now_host.loader
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        initSubscribers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        stock_receive_now_host_back.setOnClickListener {
            showConfirmDialog("Are you sure you want to cancel receive stocks?", onConfirmed = {
                viewModel.clearData()
                flow.pop(this)
            })
        }

        // Next button handler
        stock_receive_now_host_next_button.setOnClickListener {
            var allowGo = true

            if (stock_receive_now_host_viewpager.currentItem == 0) {
                if (StockReceiveNowViewModel.supplierData.supplier == null) {
                    errorDialog(getString(R.string.warning_select_supplier))
                    allowGo = false
                }

                if (allowGo) {
                    stock_receive_now_host_next_button.text = getString(R.string.next)
                    stock_receive_now_host_indicator_2.visibility = View.VISIBLE
                }

            } else if (stock_receive_now_host_viewpager.currentItem == 1) {
                if (StockReceiveNowViewModel.supplierData.products.isNullOrEmpty()) {
                    errorDialog(getString(R.string.warning_add_product))
                    allowGo = false
                }

                if (allowGo) {
                    stock_receive_now_host_next_button.text = getString(R.string.done)
                    stock_receive_now_host_indicator_3.visibility = View.VISIBLE
                }
            }

            //    productsFragment?.updateView()

            if (allowGo)
                stock_receive_now_host_viewpager.currentItem += 1
        }

        initTabLayout()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeObservers()
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }

    private fun subscribeObservers() {
        viewModel.validationResponseLiveData.observe(this, {

        })

        viewModel.getSuppliersActionLiveData.observe(this, {

        })
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun initTabLayout() {
        supplierFragment = StockReceiveSupplierFragment.newInstance()
        productsFragment = StockReceiveProductsFragment.newInstance()

        val adapter = AdapterTabPager(activity)
        adapter.addFragment(supplierFragment!!, getString(R.string.supplier))
        adapter.addFragment(productsFragment!!, getString(R.string.products))
        adapter.addFragment(StockReceiveSupplierFragment.newInstance(), getString(R.string.confirm))

        stock_receive_now_host_viewpager.isUserInputEnabled = false
        stock_receive_now_host_viewpager.adapter = adapter
        stock_receive_now_host_viewpager.currentItem = 0
        TabLayoutMediator(stock_receive_now_host_tablayout, stock_receive_now_host_viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

        stock_receive_now_host_tablayout.touchables.forEach { it.isClickable = false }
    }

    /**
     * Tab pager adapter
     */
    internal inner class AdapterTabPager(activity: FragmentActivity?) : FragmentStateAdapter(activity!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()

        fun getTabTitle(position : Int): String{
            return mFragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getItemCount(): Int {
            return mFragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return mFragmentList[position]
        }
    }

}