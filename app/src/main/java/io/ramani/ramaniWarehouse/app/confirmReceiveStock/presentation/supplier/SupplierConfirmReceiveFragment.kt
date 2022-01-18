package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.supplier

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setArgs
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow.ReceiveStockFlow
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow.ReceiveStockFlowController
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
import kotlinx.android.synthetic.main.fragment_supplier_confirm_receive.*
import org.kodein.di.generic.factory

private const val CREATED_AT_ARG = "created_at_arg"
private const val SUPPLIER_NAME_ARG = "supplier_name_arg"
const val PURCHASE_ID_ARG = "purchase_id_arg"

class SupplierConfirmReceiveFragment : BaseFragment() {
    companion object {
        fun newInstance(createdAt: String?, supplierName: String?, purchaseId: String?) =
            SupplierConfirmReceiveFragment().apply {
                setArgs(
                    CREATED_AT_ARG to createdAt,
                    SUPPLIER_NAME_ARG to supplierName,
                    PURCHASE_ID_ARG to purchaseId
                )
            }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_supplier_confirm_receive
    private val viewModelProvider: (Fragment) -> ConfirmReceiveViewModel by factory()
    private lateinit var viewModel: ConfirmReceiveViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: ReceiveStockFlow

    private var createdAt: String? = ""
    private var supplierName: String? = ""
    private var purchaseId: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        flow = ReceiveStockFlowController(baseActivity!!)
        initArgs()
        subscribeObservers()
        viewModel.start()
    }

    private fun initArgs() {
        createdAt = arguments?.getString(CREATED_AT_ARG, "")
        supplierName = arguments?.getString(SUPPLIER_NAME_ARG, "")
        purchaseId = arguments?.getString(PURCHASE_ID_ARG, "")
    }

    private fun subscribeObservers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel, this)
        subscribeError(viewModel)
        observerError(viewModel, this)
    }

    override fun initView(view: View?) {
        super.initView(view)
        showInvoice()
        supplier_receiving_date_label.text = createdAt
        supplier_receiving_tv.text = supplierName
        proforma_invoice_view.setOnClickListener {
            flow.openInvoiceFragment(purchaseId ?: "")
        }
    }

    private fun showInvoice() {
        val data = viewModel.getUrl(RECEIVE_MODELS.invoiceModelView?.purchaseOrderId)
        proforma_invoice_iv.fromUrl(data.first, data.second)
    }
}