package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import kotlinx.android.synthetic.main.fragment_stock_success.*
import org.kodein.di.generic.factory

class StockReceiveSuccessFragment : BaseFragment() {
    companion object {
        const val PARAM_RETURNED_DATA = "Returned_Stock"

        fun newInstance(goodsReceivedModel: GoodsReceivedModel): StockReceiveSuccessFragment {
            val fragment = StockReceiveSuccessFragment()

            val args = Bundle()
            args.putParcelable(PARAM_RETURNED_DATA, goodsReceivedModel)

            fragment.arguments = args
            return fragment
        }
    }

    private val viewModelProvider: (Fragment) -> StockReceiveNowViewModel by factory()
    private lateinit var viewModel: StockReceiveNowViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_success

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)

    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        stock_receive_success_print.setOnSingleClickListener {

        }
    }
}