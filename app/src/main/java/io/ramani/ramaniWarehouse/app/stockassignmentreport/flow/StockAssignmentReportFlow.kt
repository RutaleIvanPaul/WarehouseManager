package io.ramani.ramaniWarehouse.app.stockassignmentreport.flow

import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel

interface StockAssignmentReportFlow {
    fun openDetail(isAssignedStock: Boolean, stock: StockAssignmentReportDistributorDateModel)
    fun openPrint()
}