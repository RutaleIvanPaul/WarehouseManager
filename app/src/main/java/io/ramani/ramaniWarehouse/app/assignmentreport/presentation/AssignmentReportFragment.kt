package io.ramani.ramaniWarehouse.app.assignmentreport.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignmentreport.flow.AssignmentReportFlow
import io.ramani.ramaniWarehouse.app.assignmentreport.flow.AssignmentReportFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.adapters.TabPagerAdapter
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.fragment_assignment_report.*
import org.kodein.di.generic.factory

class AssignmentReportFragment : BaseFragment() {
    companion object {
        fun newInstance() = AssignmentReportFragment()
    }

    private val viewModelProvider: (Fragment) -> AssignmentReportViewModel by factory()
    private lateinit var viewModel: AssignmentReportViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: AssignmentReportFlow

    override fun getLayoutResId(): Int = R.layout.fragment_assignment_report

    private var assignedFragment: AssignmentReportPageFragment? = null
    private var returnedFragment: AssignmentReportPageFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        initSubscribers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = AssignmentReportFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        assignment_report_back.setOnSingleClickListener {
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
        assignedFragment = AssignmentReportPageFragment.newInstance(true)
        returnedFragment = AssignmentReportPageFragment.newInstance(false)

        val adapter = TabPagerAdapter(activity)
        adapter.addFragment(assignedFragment!!, getString(R.string.assigned))
        adapter.addFragment(returnedFragment!!, getString(R.string.returned))

        assignment_report_viewpager.isUserInputEnabled = false
        assignment_report_viewpager.adapter = adapter
        assignment_report_viewpager.currentItem = 0

        TabLayoutMediator(assignment_report_tablayout, assignment_report_viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

}