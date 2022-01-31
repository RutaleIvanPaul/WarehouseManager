package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.MainNavFragment
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import kotlinx.android.synthetic.main.fragment_stock_receive_print.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.util.*

class StockReceivePrintFragment : BaseFragment() {
    companion object {
        private const val PARAM_STOCK = "stock"

        fun newInstance(stock: GoodsReceivedModel) = StockReceivePrintFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PARAM_STOCK, stock)
            }
        }
    }

    private val viewModelProvider: (Fragment) -> StockReceiveNowViewModel by factory()
    private lateinit var viewModel: StockReceiveNowViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_print

    private val dateFormatter: DateFormatter by instance()
    private var stock: GoodsReceivedModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setFormat(PixelFormat.RGBA_8888)

        viewModel = viewModelProvider(this)
        viewModel.start()

        arguments?.getParcelable<GoodsReceivedModel>(PARAM_STOCK)?.let {
            stock = it
        }

    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)

        val supplierData = STOCK_RECEIVE_MODEL.supplierData
        supplierData.apply {
            stock_receive_print_distributor_name.text = this.supplier?.name ?: ""

            date?.let {
                stock_receive_print_issued_date.text = String.format(
                    Locale.getDefault(),
                    "Date : %s",
                    dateFormatter.convertToCalendarFormatDate(it.time)
                )
            }

            documents?.let {

            }

            storeKeeperData?.let {
                stock_receive_print_store_keeper_name.text = it.name
                stock_receive_print_store_keeper_signature.setImageBitmap(it.bitmap)
            }

            deliveryPersonData?.let {
                stock_receive_print_delivery_person_name.text = it.name
                stock_receive_print_delivery_person_signature.setImageBitmap(it.bitmap)
            }

            products?.let {
                stock_receive_print_product_list.apply {
                    val layoutManagerWithDisabledScrolling =
                        object : LinearLayoutManager(requireContext()) {
                            override fun canScrollVertically(): Boolean {
                                return false
                            }
                        }
                    layoutManager = layoutManagerWithDisabledScrolling
                    adapter = StockReceivePrintRVAdapter(it as MutableList<SelectedProductModel>)
                    addItemDecoration(
                        DividerItemDecoration(
                            requireActivity(),
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            }
        }

        stock_receive_print_print_button.setOnSingleClickListener {
            val scrollView = stock_receive_print_scrollview
            val bitmap =
                Bitmap.createBitmap(scrollView.width, scrollView.getChildAt(0).height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            scrollView.draw(canvas)
            viewModel.printBitmap(bitmap)
        }

        stock_receive_print_done_button.setOnSingleClickListener {
            STOCK_RECEIVE_MODEL.clearData()

            (requireActivity() as BaseActivity).navigationManager?.popToFragment(
                MainNavFragment.TAG,
                false
            )
        }
    }

    override fun onBackButtonPressed(): Boolean {
        return true
    }

}