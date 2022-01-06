package io.ramani.ramaniWarehouse.app.stockreceive.flow

import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowHostFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSignaturePadSheetFragment
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

    override fun openSignaturePad(what: String) {
        val fragment = StockReceiveSignaturePadSheetFragment.newInstance(what)
        activity?.supportFragmentManager?.let { fragment.show(it, "signature_pad") }
    }
}