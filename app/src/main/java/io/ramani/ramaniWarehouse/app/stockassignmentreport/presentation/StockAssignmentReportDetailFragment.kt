package io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.ConfirmAssignedStockViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlow
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.ProductReceivedItemModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import kotlinx.android.synthetic.main.fragment_assignment_report_detail.assignment_report_detail_back
import kotlinx.android.synthetic.main.fragment_assignment_report_detail.assignment_report_detail_delivery_person_signature
import kotlinx.android.synthetic.main.fragment_assignment_report_detail.assignment_report_detail_end
import kotlinx.android.synthetic.main.fragment_assignment_report_detail.assignment_report_detail_issued_date
import kotlinx.android.synthetic.main.fragment_assignment_report_detail.assignment_report_detail_items_container
import kotlinx.android.synthetic.main.fragment_assignment_report_detail.assignment_report_detail_store_keeper_signature
import kotlinx.android.synthetic.main.fragment_assignment_report_detail.assignment_report_detail_title
import kotlinx.android.synthetic.main.fragment_return_receipt.*
import kotlinx.android.synthetic.main.fragment_stock_assignment_report_detail.*
import kotlinx.android.synthetic.main.item_assignment_report_detail_item_row.view.*
import org.kodein.di.generic.factory

class StockAssignmentReportDetailFragment : BaseFragment() {

    companion object {
        private const val PARAM_IS_ASSIGNED_STOCK = "isAssignedStock"
        private const val PARAM_STOCK = "stock"

        fun newInstance(isAssignedStock: Boolean, stock: StockAssignmentReportDistributorDateModel) = StockAssignmentReportDetailFragment().apply {
            arguments = Bundle().apply {
                putBoolean(PARAM_IS_ASSIGNED_STOCK, isAssignedStock)
                putParcelable(PARAM_STOCK, stock)
            }
        }
    }

    private val viewModelProvider: (Fragment) -> StockAssignmentReportViewModel by factory()
    private lateinit var viewModel: StockAssignmentReportViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockAssignmentReportFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_assignment_report_detail

    private var isAssignedStock = true
    private var stock: StockAssignmentReportDistributorDateModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()

        arguments?.getBoolean(PARAM_IS_ASSIGNED_STOCK)?.let {
            isAssignedStock = it
        }

        arguments?.getParcelable<StockAssignmentReportDistributorDateModel>(PARAM_STOCK)?.let {
            stock = it
        }

    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockAssignmentReportFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        assignment_report_detail_back.setOnSingleClickListener {
            pop()
        }


        assignment_report_detail_title.text = getString(if (isAssignedStock) R.string.start_goods_issued else R.string.start_goods_returned)
        assignment_report_detail_end.text = getString(if (isAssignedStock) R.string.end_goods_issued else R.string.end_goods_returned)

        stock?.let {
            if (!it.storeKeeperSignature.isNullOrEmpty())
                assignment_report_detail_store_keeper_signature.loadImage(it.salesPersonSignature)
            if (!it.salesPersonSignature.isNullOrEmpty())
                assignment_report_detail_delivery_person_signature.loadImage(it.salesPersonSignature)


            companyName.text = viewModel.companyName
            assignment_report_detail_issued_date.text = "Date: " + it.dateStockTaken.split("T")?.get(0) ?: ""
            assignment_report_detail_store_keeper_name.text = it.assigner
            assignment_report_detail_delivery_person_name.text = it.name

            it.listOfProducts.let {
                for (item in it) {
                    addItems(item)
                }
            }
        }


        assignment_report_detail_print_button.setOnClickListener{
            printAssignmentReceipt(viewModel)
        }

    }

    private fun addItems(item: ProductReceivedItemModel) {
        val itemView = LinearLayout.inflate(requireContext(), R.layout.item_assignment_report_detail_item_row, null)
        itemView.assignment_report_detail_item_row_name.text = item.productName
        itemView.assignment_report_detail_item_row_quantity.text = item.quantity.toString() + " pcs"
        assignment_report_detail_items_container.addView(itemView)
    }

    private fun printAssignmentReceipt(viewModel: StockAssignmentReportViewModel) {
        viewModel.printText(getString(R.string.start_of_goods_issued)+"\n\n")
//        viewModel.printBitmap(AssignedItemDetails.signatureInfoStoreKeeper!!)
//        viewModel.printText(getTextBeforeImages())
//        viewModel.printText(getString(R.string.store_keeper)+": "+storekeeper_text.text.toString()+ "\n")
//        viewModel.printBitmap(AssignedItemDetails.signatureInfoStoreKeeper!!)
//        viewModel.printText(getString(R.string.assigned_to)+": "+assignee_text.text.toString()+ "\n")
//        viewModel.printBitmap(AssignedItemDetails.signatureInfoSalesPerson!!)
        viewModel.printText("\n"+getString(R.string.end_of_goods_assigned)+"\n\n\n\n\n")

    }

    private fun getTextBeforeImages() =
        getString(R.string.start_of_goods_issued)+"\n\n"+
                company_name.text.toString()+"\n\n"+
                getString(R.string.goods_issued_note)+"\n\n"+
                date.text.toString()+"\n"+
                "--------------------------------"+"\n"+
                getString(R.string.goods_issued) + "\n"+
                "--------------------------------"+"\n\n"+
                getGoodsAssignedString()


    private fun getGoodsAssignedString(): String {
        var GoodsAssignedText = ""
        AssignedItemDetails.assignedItems.forEach { item ->
            GoodsAssignedText += item.name +" ---------- "+item.assignedNumber+" Pcs\n"
        }

        return GoodsAssignedText
    }

}