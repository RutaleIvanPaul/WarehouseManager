package io.ramani.ramaniWarehouse.app.stockreport.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.app.stockreport.flow.StockReportFlow
import io.ramani.ramaniWarehouse.app.stockreport.flow.StockReportFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import kotlinx.android.synthetic.main.fragment_stock_report_page.*
import java.util.Calendar

class StockReportPageFragment : BaseFragment() {
    companion object {
        private const val PARAM_IS_ONLY_ASSIGNED = "isOnlyAssigned"

        fun newInstance(isOnlyAssigned: Boolean) = StockReportPageFragment().apply {
            arguments = Bundle().apply {
                putBoolean(PARAM_IS_ONLY_ASSIGNED, isOnlyAssigned)
            }
        }
    }

    private val viewModelProvider: (Fragment) -> StockReportViewModel by factory()
    private lateinit var viewModel: StockReportViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReportFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_report_page

    private val dateFormatter: DateFormatter by instance()

    private var isOnlyAssigned = true
    private lateinit var listAdapter: StockReportRVAdapter
    private var datas: ArrayList<DistributorDateModel>? = null
    private var calendar = Calendar.getInstance()

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
        flow = StockReportFlowController(baseActivity!!, R.id.main_fragment_container)
        viewModel.start()

        // Initialize UI
        updatePickDate()

        stock_report_datepick_layout.setOnClickListener {
            DatePickerDialog(requireActivity(),
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
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
            stock_report_no_stock.visibility = if (it.isEmpty()) View.VISIBLE else View.INVISIBLE

            datas = it as ArrayList<DistributorDateModel>?

            stock_report_list.apply {
                layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

                listAdapter = StockReportRVAdapter(datas!!){
                    flow.openDetail(isOnlyAssigned, it)
                }

                adapter = listAdapter
            }
        })
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        stock_report_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun updatePickDate() {
        val timeString = dateFormatter.convertToDateWithDashes1(calendar.time.time)
        stock_report_pick_date.text = timeString
        viewModel.getDistributorDate(timeString, isOnlyAssigned)
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updatePickDate()
        }

}