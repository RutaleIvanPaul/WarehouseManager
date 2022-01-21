package io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlow
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.adapters.TabPagerAdapter
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.fragment_stock_assignment_report.*
import org.kodein.di.generic.factory

class StockStockAssignmentReportFragment : BaseFragment() {
    companion object {
        fun newInstance() = StockStockAssignmentReportFragment()
    }

    private val viewModelProvider: (Fragment) -> StockAssignmentReportViewModel by factory()
    private lateinit var viewModel: StockAssignmentReportViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockAssignmentReportFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_assignment_report

    private var assignedFragment: StockStockAssignmentReportPageFragment? = null
    private var returnedFragment: StockStockAssignmentReportPageFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        initSubscribers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockAssignmentReportFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        stock_report_back.setOnSingleClickListener {
            pop()
        }

        initTabLayout()
        stock_report_title.setText(R.string.assignment_report)

    }

    private fun initSubscribers() {
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.validationResponseLiveData.observe(this, {

        })
    }

    private fun initTabLayout() {
        assignedFragment = StockStockAssignmentReportPageFragment.newInstance(true)
        returnedFragment = StockStockAssignmentReportPageFragment.newInstance(false)

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