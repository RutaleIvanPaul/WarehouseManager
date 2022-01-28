package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveReceipt

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlow
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.presentation.MainNavFragment
import kotlinx.android.synthetic.main.fragment_receive_receipt.*
import org.kodein.di.generic.factory

class ReceiveReceiptFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> ConfirmReceiveViewModel by factory()
    private lateinit var viewModel: ConfirmReceiveViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var receiptItemsAdapter: ReceiptItemsAdapter
    private lateinit var flow: AuthFlow


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setFormat(PixelFormat.RGBA_8888)
        viewModel = viewModelProvider(this)
        viewModel.start()
    }

    override fun initView(view: View?) {
        flow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)
        initPrintingView()
        receiptItemsAdapter =
            ReceiptItemsAdapter(
                RECEIVE_MODELS.invoiceModelView?.products?.toMutableList() ?: mutableListOf()
            )
        val layoutManagerWithDisabledScrolling =
            object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        returned_items_RV.layoutManager = layoutManagerWithDisabledScrolling
        returned_items_RV.adapter = receiptItemsAdapter



        return_stock_print_receipt.setOnClickListener {
            val bitmap =
                Bitmap.createBitmap(
                    scrollview.width,
                    scrollview.getChildAt(0).height,
                    Bitmap.Config.ARGB_8888
                )
            val canvas = Canvas(bitmap)
            scrollview.draw(canvas)
            viewModel.printBitmap(bitmap)
        }

        return_stock_done.setOnClickListener {
            (requireActivity() as BaseActivity).navigationManager?.popToFragment(
                MainNavFragment.TAG,
                false
            )
            RECEIVE_MODELS.reset()
        }
    }

    private fun initPrintingView() {
        if (viewModel.loggedInUser != null) {
            company_name.text = (viewModel.loggedInUser.companyName)
            date.text = "Date: ${viewModel.getNowCalendarDate()}"
            storekeeper_text.text = (RECEIVE_MODELS.invoiceModelView?.storeKeeperName)
            assignee_text.text = (RECEIVE_MODELS.invoiceModelView?.deliveryPersonName)
            storekeeper_image.setImageBitmap(RECEIVE_MODELS.invoiceModelView?.storeKeeperSign)
            assignee_image.setImageBitmap(RECEIVE_MODELS.invoiceModelView?.deliveryPersonSign)
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_receive_receipt

    companion object {
        @JvmStatic
        fun newInstance() =
            ReceiveReceiptFragment()
    }
}