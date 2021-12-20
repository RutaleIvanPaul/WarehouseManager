package io.ramani.ramaniWarehouse.app.assignmentreport.flow

import io.ramani.ramaniWarehouse.app.auth.presentation.LoginFragment
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import org.jetbrains.anko.AnkoLogger

class AssignmentReportFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : AssignmentReportFlow, AnkoLogger {

    override fun openPrint() {
        val fragment = LoginFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

}