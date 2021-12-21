package io.ramani.ramaniWarehouse.app.assignstock.presentation.host

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonFragment
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonViewModel
import io.ramani.ramaniWarehouse.app.common.presentation.adapters.TabPagerAdapter
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.tabs.StockReceiveProductsFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.tabs.StockReceiveSupplierFragment
import kotlinx.android.synthetic.main.fragment_assign_stock.*
import kotlinx.android.synthetic.main.fragment_assign_stock.assign_stock_host_next_button

import org.jetbrains.anko.backgroundDrawable
import org.kodein.di.generic.factory

class AssignStockFragment : BaseFragment() {

    companion object {
        fun newInstance() = AssignStockFragment()
    }

    private val viewModelProvider: (Fragment) -> AssignStockViewModel by factory()
    private lateinit var viewModel: AssignStockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }

    private var salespersonFragment: AssignStockSalesPersonFragment? = null
    private var productsFragment: StockReceiveProductsFragment? = null


    override fun getLayoutResId() = R.layout.fragment_assign_stock


    override fun initView(view: View?) {
        super.initView(view)
        viewModel = ViewModelProvider(this).get(AssignStockViewModel::class.java)
        initTabLayout()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        AssignStockSalesPersonViewModel.selectedSalespersonLiveData.observe(this, {
            if (it != null) {
                assign_stock_host_next_button.apply {
                    isEnabled = true
                    backgroundDrawable =
                        getDrawable(requireContext(), R.drawable.green_stroke_action_button)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_lime_yellow
                        )
                    )
                }
            } else {
                assign_stock_host_next_button.apply {
                    isEnabled = false
                    backgroundDrawable =
                        getDrawable(requireContext(), R.drawable.grey_stroke_next_action_button)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey_inactive_button_text
                        )
                    )
                }
            }
        })
    }

    private fun initTabLayout() {
        salespersonFragment = AssignStockSalesPersonFragment.newInstance()
        productsFragment = StockReceiveProductsFragment.newInstance()

        val adapter = TabPagerAdapter(activity)
        adapter.addFragment(salespersonFragment!!, getString(R.string.salesperson))
        adapter.addFragment(productsFragment!!, getString(R.string.products))
        adapter.addFragment(StockReceiveSupplierFragment.newInstance(), getString(R.string.confirm))

        assign_stock_viewpager.adapter = adapter
        assign_stock_viewpager.currentItem = 0
        TabLayoutMediator(assign_stock_tablayout, assign_stock_viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

    }

}