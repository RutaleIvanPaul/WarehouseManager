package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.successReceive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlow
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlowcontroller
import kotlinx.android.synthetic.main.fragment_return_success.*
import org.kodein.di.generic.factory

class ReceiveSuccessFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> ReceiveSuccessViewModel by factory()
    private lateinit var viewModel: ReceiveSuccessViewModel
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
        return_stock_view_receipt.setOnClickListener {
            flow.openReturnedStockPrintScreen()
        }
    }


    override fun getLayoutResId(): Int = R.layout.fragment_return_success



    companion object {
        fun newInstance() =
            ReceiveSuccessFragment()
    }
}