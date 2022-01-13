package io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlow
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlowcontroller
import kotlinx.android.synthetic.main.fragment_assign_success.*
import org.kodein.di.generic.factory

class AssignSuccessFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> AssignSuccessViewModel by factory()
    private lateinit var viewModel: AssignSuccessViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow:ReturnStockFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = ReturnStockFlowcontroller(baseActivity!!, R.id.main_fragment_container)
        assign_stock_view_receipt.setOnClickListener {
            flow.openReturnedStockPrintScreen()
        }
    }


    override fun getLayoutResId(): Int = R.layout.fragment_assign_success



    companion object {
        fun newInstance() =
            AssignSuccessFragment()
    }
}