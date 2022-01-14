package io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import java.math.RoundingMode.valueOf
import java.security.Timestamp
import java.text.SimpleDateFormat


class StockAssignmentReportRVAdapter(
    data: MutableList<StockAssignmentReportDistributorDateModel>,
    val onItemClick: (StockAssignmentReportDistributorDateModel) -> Unit
) :
    BaseQuickAdapter<StockAssignmentReportDistributorDateModel, BaseViewHolder>(R.layout.item_stock_assignment_report_row, data) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun convert(helper: BaseViewHolder, item: StockAssignmentReportDistributorDateModel) {

        with(helper) {
            setText(R.id.item_stock_assignment_report_row_name, item.name)
            setText(R.id.item_stock_assignment_report_row_time, item.timestamp)

            helper.itemView.setOnSingleClickListener {
                onItemClick(item)
            }
        }
    }
}