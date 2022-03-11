package io.ramani.ramaniWarehouse.app.stockassignmentreport.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.io.toFile
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlow
import io.ramani.ramaniWarehouse.app.stockassignmentreport.flow.StockAssignmentReportFlowController
import io.ramani.ramaniWarehouse.domain.datetime.formatTimeStampFromServerToCalendarFormat
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.ProductReceivedItemModel
import io.ramani.ramaniWarehouse.domain.stockassignmentreport.model.StockAssignmentReportDistributorDateModel
import kotlinx.android.synthetic.main.fragment_stock_assignment_report_detail.*
import org.kodein.di.generic.factory

class StockAssignmentReportDetailFragment : BaseFragment() {

    companion object {
        private const val PARAM_IS_ASSIGNED_STOCK = "isAssignedStock"
        private const val PARAM_STOCK = "stock"

        fun newInstance(
            isAssignedStock: Boolean,
            stock: StockAssignmentReportDistributorDateModel
        ) = StockAssignmentReportDetailFragment().apply {
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

    private lateinit var assignmentItemsAdapter: AssignmentReportItemsAdapter

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
        assignmentItemsAdapter =
            AssignmentReportItemsAdapter(listOfProductsToPrint) {}
        viewModel = viewModelProvider(this)
        viewModel.start()

        arguments?.getBoolean(PARAM_IS_ASSIGNED_STOCK)?.let {
            isAssignedStock = it
        }

        arguments?.getParcelable<StockAssignmentReportDistributorDateModel>(PARAM_STOCK)?.let {
            stock = it
        }
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel, this)
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockAssignmentReportFlowController(baseActivity!!, R.id.main_fragment_container)

        val layoutManagerWithDisabledScrolling =
            object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

        stock_assignment_report_items_RV.layoutManager = layoutManagerWithDisabledScrolling
        stock_assignment_report_items_RV.adapter = assignmentItemsAdapter

        // Back button handler
        assignment_report_detail_back.setOnSingleClickListener {
            pop()
        }


        assignment_report_detail_title.text =
            getString(if (isAssignedStock) R.string.start_of_goods_assigned else R.string.start_goods_returned)
        assignment_report_detail_end.text =
            getString(if (isAssignedStock) R.string.end_of_goods_assigned else R.string.end_goods_returned)
        stock_assignment_report_note_text.text =
            getString(if (isAssignedStock) R.string.goods_issued_note else R.string.goods_returned_note)

        stock?.let {
            if (!it.storeKeeperSignature.isNullOrEmpty())
                assignment_report_detail_store_keeper_signature.loadImage(it.storeKeeperSignature) {
                    viewModel.isFirstPartySignatureLoaded = true
                    viewModel.checkPartiesSignatures()
                }
            if (!it.salesPersonSignature.isNullOrEmpty())
                assignment_report_detail_delivery_person_signature.loadImage(it.salesPersonSignature) {
                    viewModel.isSecondPartySignatureLoaded = true
                    viewModel.checkPartiesSignatures()
                }


            companyName.text = viewModel.companyName
            assignment_report_detail_issued_date.text =
                "Date: " + formatTimeStampFromServerToCalendarFormat(it.dateStockTaken) ?: ""
            assignment_report_detail_store_keeper_name.text = it.assigner
            assignment_report_detail_delivery_person_name.text = it.name

            salesPersonSignature = it.salesPersonSignature
            storeKeeperSignature = it.storeKeeperSignature
            storeKeeperName = it.assigner
            salesPersonName = it.name

            it.listOfProducts.let {
                listOfProductsToPrint.addAll(it.toMutableList())

            }
        }

        assignment_report_detail_print_button.setOnClickListener {
            val scrollView = scrollview
            val bitmap =
                Bitmap.createBitmap(
                    scrollView.width,
                    scrollView.getChildAt(0).height,
                    Bitmap.Config.ARGB_8888
                )
            val canvas = Canvas(bitmap)
            scrollView.draw(canvas)
            viewModel.printBitmap(bitmap)
            bitmap.toFile(requireContext(), "assignment_report")

        }

    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }


}