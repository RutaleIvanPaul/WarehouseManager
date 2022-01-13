package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveReceipt

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlow
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
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
        returned_items_RV.layoutManager = LinearLayoutManager(requireContext())
        returned_items_RV.adapter = receiptItemsAdapter



        return_stock_print_receipt.setOnClickListener {
            val view = scrollview
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
//            val bgDrawable = view.background
//            if (bgDrawable!=null){
//                bgDrawable.draw(canvas)
//            }
//            else{
//                canvas.drawColor(Color.WHITE)
//            }
            view.draw(canvas)
            viewModel.printBitmap(bitmap)
//
//            val scaledBitmap = Bitmap.createScaledBitmap(bitmap,400,view.height,false)

//            printReturnReceipt()
        }

        return_stock_done.setOnClickListener {
            flow.openMainNav()
        }
    }

//    private fun printReturnReceipt() {
//        viewModel.printText(getTextBeforeImages())
//        viewModel.printText(getString(R.string.store_keeper) + ": " + storekeeper_text.text.toString() + "\n")
//        viewModel.printBitmap(ReturnStockViewModel.returnItemDetails.signatureInfoStoreKeeper!!)
//        viewModel.printText(getString(R.string.assigned_to) + ": " + assignee_text.text.toString() + "\n")
//        viewModel.printBitmap(ReturnStockViewModel.returnItemDetails.signatureInfoSalesPerson!!)
//        viewModel.printText("\n" + getString(R.string.end_goods_returned) + "\n\n\n\n\n")
//
//    }

//    private fun getTextBeforeImages() =
//        getString(R.string.start_of_goods_returned) + "\n\n" +
//                company_name.text.toString() + "\n\n" +
//                getString(R.string.goods_issued_note) + "\n\n" +
//                date.text.toString() + "\n" +
//                "--------------------------------" + "\n" +
//                getString(R.string.goods_returned) + "\n" +
//                "--------------------------------" + "\n\n" +
//                getGoodsReturnedString()


//    private fun getGoodsReturnedString(): String {
//        var goodsReturnedText = ""
//        ReturnStockViewModel.returnItemDetails.returnItems.forEach { item ->
//            goodsReturnedText += item.productName + " ---------- " + item.quantity + " Pcs\n"
//        }
//        return goodsReturnedText
//    }


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