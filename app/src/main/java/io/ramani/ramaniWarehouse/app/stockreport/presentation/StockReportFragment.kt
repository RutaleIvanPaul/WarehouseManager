package io.ramani.ramaniWarehouse.app.stockreport.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.stockreport.flow.StockReportFlow
import io.ramani.ramaniWarehouse.app.stockreport.flow.StockReportFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import kotlinx.android.synthetic.main.fragment_stock_report.*
import kotlinx.android.synthetic.main.fragment_stock_report.stock_report_datepick_layout
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.util.*

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

    private val dateFormatter: DateFormatter by instance()

    private lateinit var listAdapter: StockReportRVAdapter
    private var datas: ArrayList<DistributorDateModel>? = null
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()

        initSubscribers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReportFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        stock_report_back.setOnSingleClickListener {
            pop()
        }

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

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel, this)
        subscribeError(viewModel)
        observerError(viewModel, this)

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.validationResponseLiveData.observe(this, {

        })

        viewModel.validationResponseLiveData.observe(this, {

        })

        viewModel.getDistributorDateActionLiveData.observe(this, {
            stock_report_no_stock.visibility = if (it.isEmpty()) View.VISIBLE else View.INVISIBLE

            datas = it as ArrayList<DistributorDateModel>?

            stock_report_list.apply {
                layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

                listAdapter = StockReportRVAdapter(datas!!){
                    flow.openDetail(it)
                }

                adapter = listAdapter
            }
        })
    }

    private fun updatePickDate() {
        val timeString = dateFormatter.convertToDateWithDashes1(calendar.time.time)
        stock_report_pick_date.text = timeString
        viewModel.getDistributorDate(timeString)
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updatePickDate()
        }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        stock_report_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }
}