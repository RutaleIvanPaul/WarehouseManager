package io.ramani.ramaniWarehouse.app.assignmentreport.flow

import io.ramani.ramaniWarehouse.app.assignmentreport.presentation.AssignmentReportDetailFragment
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.AssignSuccessFragment
import io.ramani.ramaniWarehouse.app.auth.presentation.LoginFragment
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ReturnSuccessFragment
import io.ramani.ramaniWarehouse.app.returnstock.presentation.returnreceipt.ReturnReceiptFragment
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import org.jetbrains.anko.AnkoLogger

class AssignmentReportFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : AssignmentReportFlow, AnkoLogger {

    override fun openDetail(isAssignedStock: Boolean, stock: DistributorDateModel) {
        val fragment = AssignmentReportDetailFragment.newInstance(isAssignedStock, stock)
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