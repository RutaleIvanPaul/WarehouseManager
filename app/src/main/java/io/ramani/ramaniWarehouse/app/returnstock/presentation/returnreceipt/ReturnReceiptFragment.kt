package io.ramani.ramaniWarehouse.app.returnstock.presentation.returnreceipt

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlow
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ConfirmReturnItemsAdapter
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ConfirmReturnStockViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.model.ReturnItemDetails
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domainCore.date.now
import io.ramani.ramaniWarehouse.domainCore.printer.PX400Printer
import kotlinx.android.synthetic.main.fragment_return_receipt.*
import kotlinx.android.synthetic.main.fragment_return_success.*
import org.kodein.di.generic.factory

class ReturnReceiptFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> ConfirmReturnStockViewModel by factory()
    private lateinit var viewModel: ConfirmReturnStockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var confirmReturnItemsAdapter: ConfirmReturnItemsAdapter
    private lateinit var flow: AuthFlow



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        confirmReturnItemsAdapter =
            ConfirmReturnItemsAdapter(ReturnItemDetails.returnItems) {}
        viewModel = viewModelProvider(this)
        viewModel.start()
    }

    override fun initView(view: View?) {
        flow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)
        returned_items_RV.layoutManager = LinearLayoutManager(requireContext())
        returned_items_RV.adapter = confirmReturnItemsAdapter

        subscribeObservers()

        return_stock_print_receipt.setOnClickListener {
            printReturnReceipt(viewModel)
        }

        return_stock_done.setOnClickListener {
            flow.openMainNav()
        }
    }

    private fun printReturnReceipt(viewModel: ConfirmReturnStockViewModel) {
        viewModel.printText(getTextBeforeImages())
        viewModel.printText(getString(R.string.store_keeper)+": "+storekeeper_text.text.toString()+ "\n")
        viewModel.printBitmap(ReturnItemDetails.signatureInfoStoreKeeper!!)
        viewModel.printText(getString(R.string.assigned_to)+": "+assignee_text.text.toString()+ "\n")
        viewModel.printBitmap(ReturnItemDetails.signatureInfoSalesPerson!!)
        viewModel.printText("\n"+getString(R.string.end_goods_returned)+"\n\n\n\n\n")

    }

    private fun getTextBeforeImages() =
        getString(R.string.start_of_goods_returned)+"\n\n"+
                company_name.text.toString()+"\n\n"+
        getString(R.string.goods_issued_note)+"\n\n"+
                date.text.toString()+"\n"+
                "--------------------------------"+"\n"+
                getString(R.string.goods_returned) + "\n"+
                "--------------------------------"+"\n\n"+
                getGoodsReturnedString()


    private fun getGoodsReturnedString(): String {
        var goodsReturnedText = ""
        ReturnItemDetails.returnItems.forEach { item ->
            goodsReturnedText += item.productName +" ---------- "+item.quantity+" Pcs\n"
        }
        return goodsReturnedText
    }


    private fun subscribeObservers() {
        viewModel.loadedUserDetails.observe(this,{
            if (it != null) {
                company_name.setText(it.companyName)
                date.setText("Date: ${viewModel.dateFormatter.convertToCalendarFormatDate(now())}")
                storekeeper_text.setText(ReturnItemDetails.storekeeperName)
                assignee_text.setText(ReturnItemDetails.salespersonName)
                storekeeper_image.setImageBitmap(ReturnItemDetails.signatureInfoStoreKeeper)
                assignee_image.setImageBitmap(ReturnItemDetails.signatureInfoSalesPerson)
            }
        })

        observerError(viewModel, this)
    }


    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun getLayoutResId(): Int  =  R.layout.fragment_return_receipt

    companion object {
        @JvmStatic
        fun newInstance() =
            ReturnReceiptFragment()
    }
}