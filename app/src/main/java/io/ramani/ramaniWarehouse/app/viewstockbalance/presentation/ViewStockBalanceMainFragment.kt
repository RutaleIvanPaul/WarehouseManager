package io.ramani.ramaniWarehouse.app.viewstockbalance.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.viewstockbalance.flow.ViewStockBalanceFlow
import io.ramani.ramaniWarehouse.app.viewstockbalance.flow.ViewStockBalanceFlowController
import io.ramani.ramaniWarehouse.app.viewstockbalance.model.ViewStockBalanceViewModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domainCore.date.now
import kotlinx.android.synthetic.main.fragment_view_stock_balance.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.util.*

/**
 * @description     View Stock Balance Main Page
 */
class ViewStockBalanceMainFragment : BaseFragment() {
    companion object {
        fun newInstance() = ViewStockBalanceMainFragment()
    }

    private val viewModelProvider: (Fragment) -> ViewStockBalanceViewModel by factory()
    private lateinit var viewModel: ViewStockBalanceViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: ViewStockBalanceFlow
    private val dateFormatter: DateFormatter by instance()

    override fun getLayoutResId(): Int = R.layout.fragment_view_stock_balance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        viewModel.start()

        viewModel.loading.observe(this) {
            if (!it) {
                view_stock_balance_list.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter =
                        ViewStockBalanceRVAdapter(viewModel.stockBalanceModel.rows as MutableList<List<String>>)
                    addItemDecoration(
                        DividerItemDecoration(
                            requireActivity(),
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            }
        }

    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = ViewStockBalanceFlowController(baseActivity!!, R.id.main_fragment_container)

        view_stock_balance_back.setOnSingleClickListener {
            pop()
        }

        view_stock_balance_date.text = dateFormatter.convertToCalendarFormatDate(now())
        view_stock_balance_warehouse_name.text = viewModel.warehouseName
    }

    override fun onResume() {
        super.onResume()
        viewModel.getReportsQuery(dateFormatter.getFullTimeString(Date()))
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        view_stock_balance_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }
}