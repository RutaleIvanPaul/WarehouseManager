package io.ramani.ramaniWarehouse.app.main.presentation

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlow
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import org.kodein.di.generic.factory

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: AuthFlow

//    private var leftMenuActionView: ActionMenuView? = null

    private val viewModelProvider: (BaseActivity) -> MainViewModel by factory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }
        viewModel = viewModelProvider(this as BaseActivity)
        flow = AuthFlowController(this, R.id.main_fragment_container)
        subscribeError(viewModel)
        observerError(viewModel, this)
        viewModel.start()
        if (viewModel.isUserLoggedInBefore) {
            flow.openMainNav()
        } else {
            flow.openLogin()
        }
    }
}