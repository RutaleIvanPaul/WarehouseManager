package com.code95.android.app.auth.flow

import io.ramani.ramaniWarehouse.app.auth.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowHostFragment
import org.jetbrains.anko.AnkoLogger

class StockReceiveFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : StockReceiveFlow, AnkoLogger {

    override fun openReceiveNow() {
        val fragment = StockReceiveNowHostFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun pop(fragment: BaseFragment) {
        activity.navigationManager?.remove(fragment)
    }
}