package io.ramani.ramaniWarehouse.app.stockreport.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.stockreport.flow.StockReportFlow
import io.ramani.ramaniWarehouse.app.stockreport.flow.StockReportFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.adapters.TabPagerAdapter
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.fragment_stock_report.*
import org.kodein.di.generic.factory

class StockReportFragment : BaseFragment() {
    companion object {
        fun newInstance() = StockReportFragment()
    }

    private val viewModelProvider: (Fragment) -> StockReportViewModel by factory()
    private lateinit var viewModel: StockReportViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReportFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_report

    private var assignedFragment: StockReportPageFragment? = null
    private var returnedFragment: StockReportPageFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        initSubscribers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReportFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        stock_report_back.setOnSingleClickListener {
            pop()
        }

        initTabLayout()
    }

    private fun initSubscribers() {
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.validationResponseLiveData.observe(this, {

        })
    }

    private fun initTabLayout() {
        assignedFragment = StockReportPageFragment.newInstance(true)
        returnedFragment = StockReportPageFragment.newInstance(false)

        val adapter = TabPagerAdapter(activity)
        adapter.addFragment(assignedFragment!!, getString(R.string.assigned))
        adapter.addFragment(returnedFragment!!, getString(R.string.returned))

        stock_report_viewpager.isUserInputEnabled = false
        stock_report_viewpager.adapter = adapter
        stock_report_viewpager.currentItem = 0

        TabLayoutMediator(stock_report_tablayout, stock_report_viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

}