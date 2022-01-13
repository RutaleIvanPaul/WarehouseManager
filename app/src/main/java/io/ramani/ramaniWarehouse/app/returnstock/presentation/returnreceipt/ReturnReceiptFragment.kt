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
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ConfirmReturnItemsAdapter
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.ConfirmReturnStockViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domainCore.date.now
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
            ConfirmReturnItemsAdapter(ReturnStockViewModel.returnItemDetails.returnItems) {}
        viewModel = viewModelProvider(this)
        viewModel.start()
    }

    override fun initView(view: View?) {
        flow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)
        returned_items_RV.layoutManager = LinearLayoutManager(requireContext())
        returned_items_RV.adapter = confirmReturnItemsAdapter

        subscribeObservers()

        return_stock_print_receipt.setOnClickListener {
            val  view = scrollview
            val bitmap = Bitmap.createBitmap(view.width,view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val bgDrawable = view.background
            if (bgDrawable!=null){
                bgDrawable.draw(canvas)
            }
            else{
                canvas.drawColor(Color.WHITE)
            }
            view.draw(canvas)

            screenshot.setImageBitmap(bitmap)
        }

        return_stock_done.setOnClickListener {
            flow.openMainNav()
        }
    }

    private fun subscribeObservers() {
        viewModel.loadedUserDetails.observe(this,{
            if (it != null) {
                company_name.setText(it.companyName)
                date.setText("Date: ${viewModel.dateFormatter.convertToCalendarFormatDate(now())}")
                storekeeper_text.setText(ReturnStockViewModel.returnItemDetails.storekeeperName)
                assignee_text.setText(ReturnStockViewModel.returnItemDetails.salespersonName)
                storekeeper_image.setImageBitmap(ReturnStockViewModel.returnItemDetails.signatureInfoStoreKeeper)
                assignee_image.setImageBitmap(ReturnStockViewModel.returnItemDetails.signatureInfoSalesPerson)
            }
        })
    }

    override fun getLayoutResId(): Int  =  R.layout.fragment_return_receipt

    companion object {
        @JvmStatic
        fun newInstance() =
            ReturnReceiptFragment()
    }
}