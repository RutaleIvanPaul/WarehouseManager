package io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showDatePicker
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlow
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlowcontroller
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
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
        pick_date_layout.setOnClickListener {
            showDatePicker(
                Calendar.getInstance().timeInMillis,
                -1,
                -1
            ) { year, monthOfYear, dayOfMonth, timInMillis ->
                Log.d("Date",year.toString())
            }
        }

        select_salesperson_spinner.setOnClickListener {
            flow.openSalesPersonBottomSheet()
        }
    }

    override fun getLayoutResId(): Int {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance() = SalesPersonFragment()
    }
}