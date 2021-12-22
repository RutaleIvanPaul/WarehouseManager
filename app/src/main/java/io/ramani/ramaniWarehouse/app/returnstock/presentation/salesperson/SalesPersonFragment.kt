package io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showDatePicker
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlow
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlowcontroller
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.domainCore.date.now
import kotlinx.android.synthetic.main.fragment_sales_person.*
import org.kodein.di.generic.factory
import java.util.*

class SalesPersonFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> SalesPersonViewModel by factory()
    private lateinit var viewModel: SalesPersonViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: ReturnStockFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        flow = ReturnStockFlowcontroller(baseActivity!!, R.id.main_fragment_container)
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
            showDatePicker(
                Calendar.getInstance().timeInMillis,
                -1,
                -1
            ) { year, monthOfYear, dayOfMonth, timInMillis ->
                return_stock_datepicker_text.text = viewModel.getDate(timInMillis)
            }
        }

        select_salesperson_spinner.text = ""
        SalesPersonViewModel.selectedSalespersonLiveData.postValue(null)

        select_salesperson_spinner.setOnSingleClickListener {
            flow.openSalesPersonBottomSheet()
        }
        viewModel.getSalespeople()
    }

    private fun subscribeObservers() {

        SalesPersonViewModel.selectedSalespersonLiveData.observe(this,{
            if (it != null) {
                select_salesperson_spinner.text = it
                ReturnStockViewModel.allowToGoNext.postValue(Pair(0, true))
            } else {
                ReturnStockViewModel.allowToGoNext.postValue(Pair(0, false))
            }
        })
    }

    override fun getLayoutResId() = R.layout.fragment_sales_person

    companion object {
        @JvmStatic
        fun newInstance() = SalesPersonFragment()
    }
}