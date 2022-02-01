package io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.flow.AssignStockFlow
import io.ramani.ramaniWarehouse.app.assignstock.flow.AssignStockFlowcontroller
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlow
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlowController
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domainCore.date.now
import kotlinx.android.synthetic.main.fragment_sales_person.*
import kotlinx.android.synthetic.main.fragment_stock_assignment_report_page.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.util.*

class AssignmentReportSalesPersonFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> AssignmentReportSalesPersonViewModel by factory()
    private lateinit var viewModel: AssignmentReportSalesPersonViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockAssignmentReportFlow

    private val dateFormatter: DateFormatter by instance()
    private var calendar = Calendar.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        flow = StockAssignmentReportFlowController(baseActivity!!, R.id.main_fragment_container)
        subscribeObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales_person, container, false)
    }


    override fun initView(view: View?) {
        super.initView(view)
        return_stock_datepicker_text.text = viewModel.getDate(now())
        pick_date_layout.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                startDateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        assignment_report_select_salesperson_spinner.text = ""
        AssignmentReportSalesPersonViewModel.selectedSalespersonLiveData.postValue(null)


        assignment_report_select_salesperson_spinner.setOnSingleClickListener {
            flow.openAssignmentReportSalesPersonBottomSheet()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSalespeople()
        assignment_report_select_salesperson_spinner.text = ""

    }

    private fun subscribeObservers() {

        AssignStockSalesPersonViewModel.selectedSalespersonLiveData.observe(this, {
            AssignStockSalesPersonViewModel.salesPeopleList.addAll(AssignmentReportSalesPersonViewModel.salesPeopleList)

        })
        viewModel.getSalespeople()
    }

    private fun updateStartPickDate() {
        val timeString = dateFormatter.convertToDateWithDashes1(calendar.time.time)
        return_stock_datepicker_text.text = timeString
    }

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateStartPickDate()
        }

    override fun getLayoutResId() = R.layout.fragment_sales_person

    companion object {
        @JvmStatic
        fun newInstance() = AssignmentReportSalesPersonFragment()
    }
}