package io.ramani.ramaniWarehouse.app.assignmentreport.presentation

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignmentreport.flow.AssignmentReportFlow
import io.ramani.ramaniWarehouse.app.assignmentreport.flow.AssignmentReportFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.assignmentreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedItemModel
import kotlinx.android.synthetic.main.fragment_assignment_report_detail.*
import kotlinx.android.synthetic.main.item_assignment_report_detail_item_row.view.*
import org.kodein.di.generic.factory

class AssignmentReportDetailFragment : BaseFragment() {
    companion object {
        private const val PARAM_IS_ASSIGNED_STOCK = "isAssignedStock"
        private const val PARAM_STOCK = "stock"

        fun newInstance(isAssignedStock: Boolean, stock: DistributorDateModel) = AssignmentReportDetailFragment().apply {
            arguments = Bundle().apply {
                putBoolean(PARAM_IS_ASSIGNED_STOCK, isAssignedStock)
                putParcelable(PARAM_STOCK, stock)
            }
        }
    }

    private val viewModelProvider: (Fragment) -> AssignmentReportViewModel by factory()
    private lateinit var viewModel: AssignmentReportViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: AssignmentReportFlow

    override fun getLayoutResId(): Int = R.layout.fragment_assignment_report_detail

    private var isAssignedStock = true
    private var stock: DistributorDateModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)

        arguments?.getBoolean(PARAM_IS_ASSIGNED_STOCK)?.let {
            isAssignedStock = it
        }

        arguments?.getParcelable<DistributorDateModel>(PARAM_STOCK)?.let {
            stock = it
        }

    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = AssignmentReportFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        assignment_report_detail_back.setOnSingleClickListener {
            pop()
        }

        assignment_report_detail_title.text = getString(if (isAssignedStock) R.string.start_goods_issued else R.string.start_goods_returned)
        assignment_report_detail_end.text = getString(if (isAssignedStock) R.string.end_goods_issued else R.string.end_goods_returned)

        stock?.let {
            if (!it.storeKeeperSignature.isNullOrEmpty())
                assignment_report_detail_store_keeper_signature.loadImage(it.storeKeeperSignature[0])
            if (!it.deliveryPersonSignature.isNullOrEmpty())
                assignment_report_detail_delivery_person_signature.loadImage(it.deliveryPersonSignature[0])

            assignment_report_detail_issued_date.text = "Date: " + it.date

            it.items.let {
                for (item in it) {
                    addItems(item)
                }
            }
        }

    }

    private fun addItems(item: GoodsReceivedItemModel) {
        val itemView = LinearLayout.inflate(requireContext(), R.layout.item_assignment_report_detail_item_row, null)
        itemView.assignment_report_detail_item_row_name.text = item.productName
        itemView.assignment_report_detail_item_row_quantity.text = if (isAssignedStock) item.qtyAccepted.toString() + " pcs" else item.qtyDeclined.toString() + " pcs"
        assignment_report_detail_items_container.addView(itemView)
    }

}