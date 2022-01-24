package io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlow
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlowController
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import kotlinx.android.synthetic.main.fragment_stock_assignment_report_page.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.util.*

class StockStockAssignmentReportPageFragment : BaseFragment() {
    companion object {
        private const val PARAM_IS_ONLY_ASSIGNED = "isOnlyAssigned"
        private var clickPosition = true

        fun newInstance(isOnlyAssigned: Boolean) = StockStockAssignmentReportPageFragment().apply {
            arguments = Bundle().apply {
                putBoolean(PARAM_IS_ONLY_ASSIGNED, isOnlyAssigned)
                clickPosition = isOnlyAssigned
                Log.e("Here 1", clickPosition.toString())
                StockAssignmentReportViewModel.returnSelected.postValue(isOnlyAssigned)
            }
        }
    }

    private val viewModelProvider: (Fragment) -> StockAssignmentReportViewModel by factory()
    private lateinit var viewModel: StockAssignmentReportViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockAssignmentReportFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_assignment_report_page

    private val dateFormatter: DateFormatter by instance()

    private var isOnlyAssigned = true
    private lateinit var listAdapter: StockAssignmentReportRVAdapter
    private var datas: MutableList<StockAssignmentReportDistributorDateModel>? = null
    private var calendar = Calendar.getInstance()
    private var startDate: String = calendar.time.toString()
    private var endDate: String = calendar.time.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)

        arguments?.getBoolean(PARAM_IS_ONLY_ASSIGNED)?.let {
            isOnlyAssigned = it
        }

        subscribeObservers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockAssignmentReportFlowController(baseActivity!!, R.id.main_fragment_container)
        viewModel.start()

        // Initialize UI
        updateStartPickDate()
        updateEndPickDate()

        assignment_report_datepick_layout.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                startDateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        assignment_report_end_datepick_layout.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                endDateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
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

            assignment_report_no_stock.visibility =
                if (it.isEmpty()) View.VISIBLE else View.INVISIBLE

            datas = it.distinct().toMutableList()
            datas = datas!!.filter{ it.dateStockTaken >= startDate && it.dateStockTaken <= endDate}.toMutableList()

            if(isOnlyAssigned == true){
                datas = datas!!.filter{ it.stockAssignmentType == "assignment"}.toMutableList()

            }
            else {
                datas = datas!!.filter{ it.stockAssignmentType == "return"}.toMutableList()

            }



            assignment_report_list.apply {
                layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

                listAdapter = StockAssignmentReportRVAdapter(datas!!) {
                    flow.openDetail(isOnlyAssigned, it)
                }

                adapter = listAdapter
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

    private fun updateStartPickDate() {
        val timeString = dateFormatter.convertToDateWithDashes1(calendar.time.time)
        startDate = timeString
        assignment_report_pick_date.text = timeString
        viewModel.getDistributorDate(startDate, endDate, isOnlyAssigned)
    }

    private fun updateEndPickDate() {
        val timeString = dateFormatter.convertToDateWithDashes1(calendar.time.time)
        endDate = timeString
        assignment_report_end_pick_date.text = timeString
        viewModel.getDistributorDate(startDate, endDate, isOnlyAssigned)
    }

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateStartPickDate()
        }

    private val endDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateEndPickDate()
        }

}