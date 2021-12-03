package io.ramani.ramaniWarehouse.app.returnstock.presentation.host

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.SalesPersonViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveProductsFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSupplierFragment
import kotlinx.android.synthetic.main.fragment_return_stock.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.textColor
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
    private var productsFragment: StockReceiveProductsFragment? = null


    override fun getLayoutResId() = R.layout.fragment_return_stock


    override fun initView(view: View?) {
        super.initView(view)
        viewModel = ViewModelProvider(this).get(ReturnStockViewModel::class.java)
        initTabLayout()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        SalesPersonViewModel.selectedSalesperson.observe(this,{
            if(it != null){
                return_stock_host_next_button.apply {
                    isEnabled = true
                    backgroundDrawable= getDrawable(requireContext(),R.drawable.green_stroke_action_button)
                    setTextColor(ContextCompat.getColor(requireContext(),R.color.light_lime_yellow))
                }
            }
        })
    }

    private fun initTabLayout() {
        salespersonFragment = SalesPersonFragment.newInstance()
        productsFragment = StockReceiveProductsFragment.newInstance()

        val adapter = AdapterTabPager(activity)
        adapter.addFragment(salespersonFragment!!, getString(R.string.salesperson))
        adapter.addFragment(productsFragment!!, getString(R.string.products))
        adapter.addFragment(StockReceiveSupplierFragment.newInstance(), getString(R.string.confirm))

        return_stock_viewpager.adapter = adapter
        return_stock_viewpager.currentItem = 0
        TabLayoutMediator(return_stock_tablayout, return_stock_viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

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