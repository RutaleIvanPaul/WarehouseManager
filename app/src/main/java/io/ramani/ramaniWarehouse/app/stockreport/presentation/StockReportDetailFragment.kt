package io.ramani.ramaniWarehouse.app.stockreport.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockreport.flow.StockReportFlow
import io.ramani.ramaniWarehouse.app.stockreport.flow.StockReportFlowController
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.datetime.formatTimeStampFromServerToCalendarFormat
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedItemModel
import io.ramani.ramaniWarehouse.domain.stockreport.model.DistributorDateModel
import kotlinx.android.synthetic.main.fragment_stock_report_detail.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

class StockReportDetailFragment : BaseFragment() {
    companion object {
        private const val PARAM_STOCK = "stock"

        fun newInstance(stock: DistributorDateModel) = StockReportDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PARAM_STOCK, stock)
            }
        }
    }

    private val viewModelProvider: (Fragment) -> StockReportViewModel by factory()
    private lateinit var viewModel: StockReportViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReportFlow
    private val dateFormatter: DateFormatter by instance()

    override fun getLayoutResId(): Int = R.layout.fragment_stock_report_detail

    private var stock: DistributorDateModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        requireActivity().window.setFormat(PixelFormat.RGBA_8888)

        arguments?.getParcelable<DistributorDateModel>(PARAM_STOCK)?.let {
            stock = it
        }

        viewModel.start()

        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel,this)
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReportFlowController(baseActivity!!, R.id.main_fragment_container)

        // Back button handler
        stock_report_detail_back.setOnSingleClickListener {
            pop()
        }

        stock_report_detail_company_name.text = viewModel.companyName

        stock?.let {
            if (!it.storeKeeperSignature.isNullOrEmpty())
                stock_report_detail_store_keeper_signature.loadImage(it.storeKeeperSignature[0]) {
                    viewModel.isFirstPartySignatureLoaded = true
                    viewModel.checkPartiesSignatures()
                }
            if (!it.deliveryPersonSignature.isNullOrEmpty())
                stock_report_detail_delivery_person_signature.loadImage(it.deliveryPersonSignature[0]) {
                    viewModel.isSecondPartySignatureLoaded = true
                    viewModel.checkPartiesSignatures()
                }

            stock_report_detail_issued_date.text =
                "Date: " + formatTimeStampFromServerToCalendarFormat(it.date) ?: ""
            stock_report_detail_store_keeper_name.text = it.warehouseManagerName
            stock_report_detail_delivery_person_name.text = it.deliveryPersonName

            it.items.let {
                stock_report_detail_product_list.apply {
                    val layoutManagerWithDisabledScrolling =
                        object : LinearLayoutManager(requireContext()) {
                            override fun canScrollVertically(): Boolean {
                                return false
                            }
                        }
                    layoutManager = layoutManagerWithDisabledScrolling

                    adapter = StockReportDetailRVAdapter(it as MutableList<GoodsReceivedItemModel>)
                    addItemDecoration(
                        DividerItemDecoration(
                            requireActivity(),
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            }
        }

        stock_report_detail_print_button.setOnSingleClickListener {
            val scrollView = stock_report_detail_scrollview
            val bitmap = Bitmap.createBitmap(
                scrollView.width,
                scrollView.getChildAt(0).height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            val bgDrawable = scrollView.background

            if (bgDrawable!=null){
                bgDrawable.draw(canvas)
            }
            else{
                canvas.drawColor(Color.WHITE)
            }
            scrollView.draw(canvas)
            viewModel.printBitmap(bitmap)
        }

    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }
}