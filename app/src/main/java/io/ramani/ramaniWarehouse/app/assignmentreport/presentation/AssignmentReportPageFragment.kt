package io.ramani.ramaniWarehouse.app.assignmentreport.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.app.assignmentreport.flow.AssignmentReportFlow
import io.ramani.ramaniWarehouse.app.assignmentreport.flow.AssignmentReportFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.domain.auth.model.DistributorDateModel
import kotlinx.android.synthetic.main.fragment_assignment_report_page.*

class AssignmentReportPageFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> AssignmentReportViewModel by factory()
    private lateinit var viewModel: AssignmentReportViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: AssignmentReportFlow

    override fun getLayoutResId(): Int = R.layout.fragment_assignment_report_page

    private val dateFormatter: DateFormatter by instance()
    private lateinit var listAdapter: AssignmentReportRVAdapter
    private var datas: ArrayList<DistributorDateModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        subscribeObservers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = AssignmentReportFlowController(baseActivity!!, R.id.main_fragment_container)
        viewModel.start()

        // Initialize UI
        assignment_report_pick_date.text = dateFormatter.getCalendarTimeString(Date())

    }

    private fun subscribeObservers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel, this)
        subscribeError(viewModel)
        observerError(viewModel, this)

        viewModel.validationResponseLiveData.observe(this, {

        })

        viewModel.getDistributorDateActionLiveData.observe(this, {
            datas = it as ArrayList<DistributorDateModel>?

            assignment_report_list.apply {
                layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

                listAdapter = AssignmentReportRVAdapter(datas!!){

                }

                adapter = listAdapter
                addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
            }
        })
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        assignment_report_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

}