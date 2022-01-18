package io.ramani.ramaniWarehouse.app.stockreport.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.stockreport.flow.StockReportFlow
import io.ramani.ramaniWarehouse.app.stockreport.flow.StockReportFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedItemModel
import kotlinx.android.synthetic.main.fragment_stock_report_detail.*
import kotlinx.android.synthetic.main.item_stock_report_detail_item_row.view.*
import org.kodein.di.generic.factory

class StockReportDetailFragment : BaseFragment() {
    companion object {
        private const val PARAM_IS_ASSIGNED_STOCK = "isAssignedStock"
        private const val PARAM_STOCK = "stock"

        fun newInstance(isAssignedStock: Boolean, stock: DistributorDateModel) = StockReportDetailFragment().apply {
            arguments = Bundle().apply {
                putBoolean(PARAM_IS_ASSIGNED_STOCK, isAssignedStock)
                putParcelable(PARAM_STOCK, stock)
            }
        }
    }

    private val viewModelProvider: (Fragment) -> StockReportViewModel by factory()
    private lateinit var viewModel: StockReportViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReportFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_report_detail

    private var isAssignedStock = true
    private var stock: DistributorDateModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        requireActivity().window.setFormat(PixelFormat.RGBA_8888)

        arguments?.getBoolean(PARAM_IS_ASSIGNED_STOCK)?.let {
            isAssignedStock = it
        }

        arguments?.getParcelable<DistributorDateModel>(PARAM_STOCK)?.let {
            stock = it
        }

    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReportFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        stock_report_detail_back.setOnSingleClickListener {
            pop()
        }

        stock_report_detail_title.text = getString(if (isAssignedStock) R.string.start_goods_issued else R.string.start_goods_returned)
        stock_report_detail_end.text = getString(if (isAssignedStock) R.string.end_goods_issued else R.string.end_goods_returned)

        stock?.let {
            if (!it.storeKeeperSignature.isNullOrEmpty())
                stock_report_detail_store_keeper_signature.loadImage(it.storeKeeperSignature[0])
            if (!it.deliveryPersonSignature.isNullOrEmpty())
                stock_report_detail_delivery_person_signature.loadImage(it.deliveryPersonSignature[0])

            stock_report_detail_issued_date.text = "Date: " + it.date

            it.items.let {
                for (item in it) {
                    addItems(item)
                }
            }
        }

        stock_report_detail_print_button.setOnSingleClickListener {
            val scrollView = stock_report_detail_scrollview
            val bitmap = Bitmap.createBitmap(scrollView.width, scrollView.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            scrollView.draw(canvas)
            viewModel.printBitmap(bitmap)
        }

    }

    private fun addItems(item: GoodsReceivedItemModel) {
        val itemView = LinearLayout.inflate(requireContext(), R.layout.item_stock_report_detail_item_row, null)
        itemView.stock_report_detail_item_row_name.text = item.productName
        itemView.stock_report_detail_item_row_quantity.text = if (isAssignedStock) item.qtyAccepted.toString() + " pcs" else item.qtyDeclined.toString() + " pcs"
        stock_report_detail_items_container.addView(itemView)
    }

}