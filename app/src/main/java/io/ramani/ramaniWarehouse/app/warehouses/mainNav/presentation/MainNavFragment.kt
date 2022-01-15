package io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlow
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.showConfirmDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.showSelectPopUp
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
        const val TAG = "MainNavFragment"
    }

    private lateinit var flow: MainNavFlow
    private lateinit var authFlow: AuthFlow
    private val viewModelProvider: (Fragment) -> MainNavViewModel by factory()
    private lateinit var viewModel: MainNavViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun getLayoutResId(): Int = R.layout.fragment_main_nav

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        MainNavViewModel.warehousesList.clear()
    }

    override val navTag: String = TAG
    override fun initView(view: View?) {
        super.initView(view)
        flow = MainNavFlowController(baseActivity!!)
        authFlow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)
        initSubscribers()
        viewModel.loadWarehouses()
        setupNavs()
        setupMenu()
    }

    private fun setupMenu() {
        menu_iv.setOnClickListener {
            menu_iv.showSelectPopUp(listOf(getString(R.string.logout)),
                wrapWidth = true,
                onItemClick = { _, _, _ ->
                    showConfirmDialog(getString(R.string.confirm_logout), onConfirmed = {
                        viewModel.logout {
                            authFlow.openLogin()
                        }
                    })
                })
        }
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
            flow.openStockAssignmentReport()
        }
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        observeLoadingVisible(viewModel, this)
        subscribeOnWarehousesLoaded()
        subscribeOnWarehousesSelected()
    }

    private fun subscribeOnWarehousesSelected() {
        warehouse_spinner.text = MainNavViewModel.currentWarehouse?.name ?: ""
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
        MainNavViewModel.onWarehousesLoadedLiveData.observe(this, {

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