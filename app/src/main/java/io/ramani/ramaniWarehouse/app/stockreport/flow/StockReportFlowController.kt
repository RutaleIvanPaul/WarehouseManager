package io.ramani.ramaniWarehouse.app.stockreport.flow

import io.ramani.ramaniWarehouse.app.stockreport.presentation.StockReportDetailFragment
import io.ramani.ramaniWarehouse.app.auth.presentation.LoginFragment
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import org.jetbrains.anko.AnkoLogger

class StockReportFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : StockReportFlow, AnkoLogger {

    override fun openDetail(isAssignedStock: Boolean, stock: DistributorDateModel) {
        val fragment = StockReportDetailFragment.newInstance(isAssignedStock, stock)
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openPrint() {
        val fragment = LoginFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }


}