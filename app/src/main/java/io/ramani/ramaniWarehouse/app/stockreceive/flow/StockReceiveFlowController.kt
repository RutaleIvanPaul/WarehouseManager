package io.ramani.ramaniWarehouse.app.stockreceive.flow

import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowHostFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceivePrintFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSignaturePadSheetFragment
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSuccessFragment
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.MainNavFragment
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
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

    override fun openReceiveSuccessPage(goodsReceivedModel: GoodsReceivedModel) {
        val fragment = StockReceiveSuccessFragment.newInstance(goodsReceivedModel)
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openPrintPage(goodsReceivedModel: GoodsReceivedModel) {
        val fragment = StockReceivePrintFragment.newInstance(goodsReceivedModel)
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openRootPage() {
        val fragment = MainNavFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.REPLACE
        )
    }

}