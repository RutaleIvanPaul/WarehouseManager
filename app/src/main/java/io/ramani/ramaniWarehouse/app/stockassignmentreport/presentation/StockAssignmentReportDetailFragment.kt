package io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlow
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.ProductReceivedItemModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import kotlinx.android.synthetic.main.fragment_stock_assignment_report_detail.*
import kotlinx.android.synthetic.main.item_stock_report_detail_item_row.view.*
import org.kodein.di.generic.factory
import java.io.IOException
import java.net.URL

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
    private val listOfProductsToPrint = mutableListOf<ProductReceivedItemModel>()
    private var storeKeeperSignature: String? = null
    private var salesPersonSignature: String? = null
    private var storeKeeperName: String? = null
    private var salesPersonName: String? = null

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
        stock_assignment_report_note_text.text = getString(if (isAssignedStock) R.string.goods_issued_note else R.string.goods_returned_note)

        stock?.let {
            if (!it.storeKeeperSignature.isNullOrEmpty())
                assignment_report_detail_store_keeper_signature.loadImage(it.storeKeeperSignature)
            if (!it.salesPersonSignature.isNullOrEmpty())
                assignment_report_detail_delivery_person_signature.loadImage(it.salesPersonSignature)


            companyName.text = viewModel.companyName
            assignment_report_detail_issued_date.text = "Date: " + it.dateStockTaken.split("T")?.get(0) ?: ""
            assignment_report_detail_store_keeper_name.text = it.assigner
            assignment_report_detail_delivery_person_name.text = it.name

            salesPersonSignature = it.salesPersonSignature
            storeKeeperSignature = it.storeKeeperSignature
            storeKeeperName = it.assigner
            salesPersonName = it.name

            it.listOfProducts.let {
                listOfProductsToPrint.addAll(it.toMutableList())
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
        val itemView = LinearLayout.inflate(requireContext(), R.layout.item_stock_report_detail_item_row, null)
        itemView.stock_report_detail_item_row_name.text = item.productName
        itemView.stock_report_detail_item_row_quantity.text = item.quantity.toString() + " pcs"
        assignment_report_detail_items_container.addView(itemView)
    }

    private fun printAssignmentReceipt(viewModel: StockAssignmentReportViewModel) {
//        val storeKeeperUrl : URL = URL(storeKeeperSignature)
//        val salesPersonUrl : URL = URL(salesPersonSignature)
        viewModel.printText(getTextBeforeImages())
        //storeKeeperUrl.let { viewModel.printBitmap(it.toBitmap()) }
       // viewModel.printBitmap(R.mipmap.ic_company_logo)
        viewModel.printText(getString(R.string.store_keeper)+": "+storeKeeperName+ "\n")
        viewModel.printText(getString(R.string.assigned_to)+": "+salesPersonName+ "\n")
        //salesPersonUrl.let { viewModel.printBitmap(it.toBitmap()) }

        viewModel.printText("\n"+ getString(if (isAssignedStock) R.string.end_goods_issued else R.string.end_goods_returned) +"\n\n\n\n\n")

    }

    private fun getTextBeforeImages() =
        getString(if (isAssignedStock) R.string.start_goods_issued else R.string.start_goods_returned)+"\n\n"+
                viewModel.companyName+"\n\n"+
                getString(if (isAssignedStock) R.string.goods_issued_note else R.string.goods_returned_note)+"\n\n"+
                assignment_report_detail_issued_date.text.toString()+"\n"+
                "--------------------------------"+"\n"+
                getString(if (isAssignedStock) R.string.goods_issued else R.string.goods_returned)+"\n"+

                "--------------------------------"+"\n\n"+
                getProductDetailsString()


    private fun getProductDetailsString(): String {
        var GoodsAssignedText = ""
        listOfProductsToPrint.forEach { item ->
            GoodsAssignedText += item.productName +" ---------- "+item.quantity+" ${item.units}\n"
        }

        return GoodsAssignedText
    }

    fun URL.toBitmap(): Bitmap?{
        return try {
            BitmapFactory.decodeStream(openStream())
        }catch (e: IOException){
            null
        }
    }

}