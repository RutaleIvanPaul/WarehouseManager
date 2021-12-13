package io.ramani.ramaniWarehouse.app.returnstock.presentation.host

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.adapters.TabPagerAdapter
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.products.SelectReturnItemsFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveProductsFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSupplierFragment
import kotlinx.android.synthetic.main.fragment_return_stock.*
import org.jetbrains.anko.backgroundDrawable
import org.kodein.di.generic.factory

class ReturnStockFragment : BaseFragment() {

    companion object {
        fun newInstance() = ReturnStockFragment()
    }

    private val viewModelProvider: (Fragment) -> ReturnStockViewModel by factory()
    private lateinit var viewModel: ReturnStockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }

    private var salespersonFragment: SalesPersonFragment? = null
    private var productsFragment: SelectReturnItemsFragment? = null


    override fun getLayoutResId() = R.layout.fragment_return_stock


    override fun initView(view: View?) {
        super.initView(view)
        viewModel = ViewModelProvider(this).get(ReturnStockViewModel::class.java)
        initTabLayout()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        SalesPersonViewModel.selectedSalespersonLiveData.observe(this,{
            if(it != null){
                return_stock_host_next_button.apply {
                    isEnabled = true
                    backgroundDrawable= getDrawable(requireContext(),R.drawable.green_stroke_action_button)
                    setTextColor(ContextCompat.getColor(requireContext(),R.color.light_lime_yellow))
                }
            }
            else{
                return_stock_host_next_button.apply {
                    isEnabled = false
                    backgroundDrawable= getDrawable(requireContext(),R.drawable.grey_stroke_next_action_button)
                    setTextColor(ContextCompat.getColor(requireContext(),R.color.grey_inactive_button_text))
                }
            }
        })
    }

    private fun initTabLayout() {
        salespersonFragment = SalesPersonFragment.newInstance()
        productsFragment = SelectReturnItemsFragment.newInstance()

        val adapter = TabPagerAdapter(activity)
        adapter.addFragment(salespersonFragment!!, getString(R.string.salesperson))
        adapter.addFragment(productsFragment!!, getString(R.string.products))
        adapter.addFragment(StockReceiveSupplierFragment.newInstance(), getString(R.string.confirm))

        return_stock_viewpager.adapter = adapter
        return_stock_viewpager.currentItem = 0
        TabLayoutMediator(return_stock_tablayout, return_stock_viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

    }

}