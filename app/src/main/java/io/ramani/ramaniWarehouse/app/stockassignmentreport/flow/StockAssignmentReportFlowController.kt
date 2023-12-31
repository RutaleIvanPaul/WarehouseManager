package io.ramani.ramaniWarehouse.app.stockassignmentreport.flow

import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonBottomSheetFragment
import io.ramani.ramaniWarehouse.app.common.navgiation.NavigationManager
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation.AssignmentReportSalesPersonFragment
import io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation.StockAssignmentReportDetailFragment
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

    override fun openAssignmentReportSalesPersonBottomSheet() {
        val fragment = AssignStockSalesPersonBottomSheetFragment()
        activity?.supportFragmentManager?.let { fragment.show(it, "assignment_report_salesperson_sheet_fragment") }
    }

}