package io.ramani.ramaniWarehouse.app.stockreceive.presentation.host

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.fragment_stock_receive_main_others.*
import org.kodein.di.generic.factory

class StockReceiveMainOthersFragment : BaseFragment() {
    companion object {
        fun newInstance() = StockReceiveMainOthersFragment()
    }

    private val viewModelProvider: (Fragment) -> StockReceiveMainViewModel by factory()
    private lateinit var viewModel: StockReceiveMainViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_main_others

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)
        stock_receive_main_others_receive_now.setOnClickListener {
            flow.openReceiveNow()
        }
    }

}