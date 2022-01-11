package io.ramani.ramaniWarehouse.app.assignmentreport.flow

import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel

interface AssignmentReportFlow {
    fun openDetail(isAssignedStock: Boolean, stock: DistributorDateModel)
    fun openPrint()
}