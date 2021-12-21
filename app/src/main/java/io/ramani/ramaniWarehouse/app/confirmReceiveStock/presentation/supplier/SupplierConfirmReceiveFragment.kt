package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.supplier

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.BuildConfig.BASE_URL
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setArgs
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
import io.ramani.ramaniWarehouse.domain.entities.MediaMimeTypes
import kotlinx.android.synthetic.main.fragment_supplier_confirm_receive.*
import org.kodein.di.generic.factory
import java.util.*

private const val CREATED_AT_ARG = "created_at_arg"
private const val SUPPLIER_NAME_ARG = "supplier_name_arg"
private const val PURCHASE_ID_ARG = "purchase_id_arg"

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

    private var createdAt: String? = ""
    private var supplierName: String? = ""
    private var purchaseId: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
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
        val url =
            BASE_URL.plus("purchase/order/get/invoice/for/distributor/pdf?purchaseOrderId=$purchaseId")
        val pdfFileName = "pdf-${Calendar.getInstance().timeInMillis}"
        viewModel.downloadFie(
            url,
            pdfFileName,
            MediaMimeTypes.Documents.PDF.mime
        )
//        proforma_invoice_iv.loadImageWithHeaders(
//            imageUrl = url,
//            headers = (mapOf(
//                "sessionToken" to "Bearer ${viewModel.token}",
//                "client" to "-LrD30GyBIgLj8jeFZLb"
//            ))
//        )

        supplier_receiving_date_label.text = createdAt
        supplier_receiving_tv.text = supplierName

    }
}