package io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow.MainNavFlow
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow.MainNavFlowController
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
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeOnWarehousesLoaded()
    }

    private fun subscribeOnWarehousesLoaded() {

    }
}