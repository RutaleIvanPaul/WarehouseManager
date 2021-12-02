package io.ramani.ramaniWarehouse.app.auth.flow

import io.ramani.ramaniWarehouse.app.auth.presentation.LoginFragment
import io.ramani.ramaniWarehouse.app.auth.presentation.SigninBottomSheetFragment
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.MainNavFragment
import org.jetbrains.anko.AnkoLogger

class AuthFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : AuthFlow, AnkoLogger {

    override fun openLogin() {
        val fragment = LoginFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.REPLACE
        )
    }

    override fun openSigninSheet() {
        val fragment = SigninBottomSheetFragment()
        activity?.supportFragmentManager?.let { fragment.show(it, "signin_sheet_fragment") }
    }

    override fun openMainNav() {
        val fragment = MainNavFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD,
            addToBackStack = false
        )
    }
}