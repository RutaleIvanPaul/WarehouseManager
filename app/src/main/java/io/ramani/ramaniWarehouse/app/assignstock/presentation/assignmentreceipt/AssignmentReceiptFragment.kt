package io.ramani.ramaniWarehouse.app.AssignedStock.presentation.assignmentreceipt


import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.ConfirmAssignedItemsAdapter
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.ConfirmAssignedStockViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlow
import io.ramani.ramaniWarehouse.app.auth.flow.AuthFlowController
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domainCore.date.now
import kotlinx.android.synthetic.main.fragment_assign_receipt.*
import org.kodein.di.generic.factory

class AssignmentReceiptFragment : BaseFragment() {
    private val viewModelProvider: (Fragment) -> ConfirmAssignedStockViewModel by factory()
    private lateinit var viewModel: ConfirmAssignedStockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var confirmAssignedItemsAdapter: ConfirmAssignedItemsAdapter
    private lateinit var flow: AuthFlow



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        confirmAssignedItemsAdapter =
            ConfirmAssignedItemsAdapter(AssignedItemDetails.assignedItems) {}
        viewModel = viewModelProvider(this)
        viewModel.start()
    }

    override fun initView(view: View?) {
        flow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)

        val layoutManagerWithDisabledScrolling =
            object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

        assigned_items_RV.layoutManager =  layoutManagerWithDisabledScrolling
        assigned_items_RV.adapter = confirmAssignedItemsAdapter

        subscribeObservers()

        return_stock_print_receipt.setOnClickListener {
            val scrollView = scrollview
            val bitmap =
                Bitmap.createBitmap(scrollView.width, scrollView.getChildAt(0).height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            scrollView.draw(canvas)
            viewModel.printBitmap(bitmap)
        }

        return_stock_done.setOnClickListener {
            flow.openMainNav()
        }
    }

    private fun printAssignmentReceipt(viewModel: ConfirmAssignedStockViewModel) {
        viewModel.printText(getTextBeforeImages())
        viewModel.printText(getString(R.string.store_keeper)+": "+AssignedItemDetails.storekeeperName+ "\n")
        viewModel.printBitmap(AssignedItemDetails.signatureInfoStoreKeeper!!)
        viewModel.printText(getString(R.string.assigned_to)+": "+AssignedItemDetails.salespersonName+ "\n")
        viewModel.printBitmap(AssignedItemDetails.signatureInfoSalesPerson!!)
        viewModel.printText("\n"+getString(R.string.end_of_goods_assigned)+"\n\n\n\n\n")

    }

    private fun getTextBeforeImages() =
        getString(R.string.start_of_goods_assigned)+"\n\n"+
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
            GoodsAssignedText += item.name +" ---------- "+item.assignedNumber+" ${item.units}\n"
        }

        return GoodsAssignedText
    }


    private fun subscribeObservers() {
        viewModel.loadedUserDetails.observe(this,{
            if (it != null) {
                company_name.setText(it.companyName)
                date.setText("Date: ${viewModel.dateFormatter.convertToDateWithDashes(now())}")
                storekeeper_text.setText(AssignedItemDetails.storekeeperName)
                assignee_text.setText(AssignedItemDetails.salespersonName)
                storekeeper_image.setImageBitmap(AssignedItemDetails.signatureInfoStoreKeeper)
                assignee_image.setImageBitmap(AssignedItemDetails.signatureInfoSalesPerson)
            }
        })

        observerError(viewModel, this)
    }


    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun getLayoutResId(): Int  =  R.layout.fragment_assign_receipt

    companion object {
        @JvmStatic
        fun newInstance() =
            AssignmentReceiptFragment()
    }
}