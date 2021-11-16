package io.ramani.ramaniWarehouse.app.main.presentation

import android.os.Bundle
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.main.flow.MainFlow
import io.ramani.ramaniWarehouse.app.main.flow.MainFlowController
import org.kodein.di.generic.factory

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var mainFlow: MainFlow

//    private var leftMenuActionView: ActionMenuView? = null

    private val viewModelProvider: (BaseActivity) -> MainViewModel by factory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = viewModelProvider(this as BaseActivity)

        mainFlow = MainFlowController(this, R.id.main_fragment_container)
        subscribeError(viewModel)
        observerError(viewModel, this)
        viewModel.start()
    }
}