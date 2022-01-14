package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.successReceive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow.ReceiveStockFlow
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow.ReceiveStockFlowController
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
import kotlinx.android.synthetic.main.fragment_return_success.*
import org.kodein.di.generic.factory

class ReceiveSuccessFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> ConfirmReceiveViewModel by factory()
    private lateinit var viewModel: ConfirmReceiveViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: ReceiveStockFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = ReceiveStockFlowController(baseActivity!!)
        return_stock_view_receipt.setOnClickListener {
            flow.openReceiveReceipt()
        }
    }


    override fun getLayoutResId(): Int = R.layout.fragment_return_success


    companion object {
        fun newInstance() =
            ReceiveSuccessFragment()
    }
}