package io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow.MainNavFlow
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow.MainNavFlowController
import kotlinx.android.synthetic.main.fragment_main_nav.*
import org.kodein.di.generic.factory

class MainNavFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainNavFragment()
    }

    private lateinit var flow: MainNavFlow
    private val viewModelProvider: (Fragment) -> MainNavViewModel by factory()
    private lateinit var viewModel: MainNavViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun getLayoutResId(): Int = R.layout.fragment_main_nav

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }


    override fun initView(view: View?) {
        super.initView(view)
        flow = MainNavFlowController(baseActivity!!)
        initSubscribers()
        viewModel.start()
        setupNavs()
    }

    private fun setupNavs() {
        receive_stock_button.setOnSingleClickListener {
            flow.openReceiveStock()
        }

        assign_stock_button.setOnSingleClickListener {
            flow.openAssignStock()
        }

        return_stock_button.setOnSingleClickListener {
            flow.openReturnStock()
        }

        stock_report_button.setOnSingleClickListener {
            flow.openStockReport()
        }

        assignment_report_button.setOnSingleClickListener {
            flow.openAssignmentReport()
        }
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeOnWarehousesLoaded()
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }


    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun subscribeOnWarehousesLoaded() {
        viewModel.onWarehousesLoadedLiveData.observe(this, {

            if (MainNavViewModel.currentWarehouse != null) {
                warehouse_spinner.text = MainNavViewModel.currentWarehouse?.name ?: ""
            }

            warehouse_spinner.setOnSingleClickListener {
                flow.openWarehousesBottomSheet()
                warehouse_spinner.text = MainNavViewModel.currentWarehouse?.name ?: ""
            }
        })
    }
}