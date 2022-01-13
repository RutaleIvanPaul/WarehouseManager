package io.ramani.ramaniWarehouse.app.stockassignmentreport.flow

import io.ramani.ramaniWarehouse.app.StockAssignmentReport.presentation.StockAssignmentReportDetailFragment
import io.ramani.ramaniWarehouse.app.assignmentreport.presentation.AssignmentReportDetailFragment
import io.ramani.ramaniWarehouse.app.auth.presentation.LoginFragment
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import org.jetbrains.anko.AnkoLogger

class StockAssignmentReportFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : StockAssignmentReportFlow, AnkoLogger {

    override fun openDetail(isAssignedStock: Boolean, stock: StockAssignmentReportDistributorDateModel) {
        val fragment = StockAssignmentReportDetailFragment.newInstance(isAssignedStock, stock)
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openPrint() {

    }

}