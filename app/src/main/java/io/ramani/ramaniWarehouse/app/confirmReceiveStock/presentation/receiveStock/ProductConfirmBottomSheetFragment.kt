package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveStock

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.BaseBottomSheetDialogFragment
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.showSelectPopUp
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import kotlinx.android.synthetic.main.fragment_product_confirm_sheet.*
import kotlinx.android.synthetic.main.fragment_signin_sheet.*
import org.kodein.di.generic.factory

//const val ARG_PRODUCT_ID = "arg_product_id"

class ProductConfirmBottomSheetFragment(
    private val productId: String,
    private val onReceiveClicked: (String) -> Unit
) : BaseBottomSheetDialogFragment() {
//    companion object {
//        fun newInstance(productId: String) = ProductConfirmBottomSheetFragment().apply {
//            setArgs(ARG_PRODUCT_ID to productId)
//        }
//    }

    private val viewModelProvider: (Fragment) -> ConfirmReceiveViewModel by factory()
    private lateinit var viewModel: ConfirmReceiveViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    //    private var selectedProductId = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_product_confirm_sheet, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
//        selectedProductId = arguments?.getString(ARG_PRODUCT_ID, "") ?: ""
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        viewModel.start()
    }


    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedProduct = RECEIVE_MODELS.invoiceModelView?.products?.find { it.productId == productId }
        product_name.text = selectedProduct?.productName
        qty_incoming.text = String.format("%.0f %s", selectedProduct?.quantity, selectedProduct?.units)
        percent_delivered.text = "0 %"

//[2022.4.19][Adrian] To improve user's convenience, I believe we have to validate values when clicking Receive button rather than realtime check
/*

var lastKnownAcceptedText = ""

//[2022AfterTextChanged { text ->
    if (text.toString() != lastKnownAcceptedText) {
        lastKnownAcceptedText = text.toString()
        if (!text.isNullOrBlank()) {
            if (text.toString().toDouble() > -1) {
                if (viewModel.validateQty(
                        selectedProduct?.quantity,
                        text.toString().toDouble(),
                        qty_declined.text.toString().toDouble()
                    )
                ) {
                    selectedProduct?.qtyAccepted =
                        qty_accepted.text.toString().toDouble()
                    qty_accepted.error = null
                    qty_declined.error = null
                    percent_delivered.text = calculatePercentage(selectedProduct)
                } else {
                    qty_accepted.error =
                        getString(R.string.qty_validation).replacePlaceHolderWithText(
                            StringsPlaceHolders.QTY_VALIDATION,
                            "${selectedProduct?.quantity}"
                        )
                }
            } else {
                qty_accepted.setText("0")
            }
        } else {
            qty_accepted.setText("0")
        }
    }
}
var lastKnownDeclineText = ""
qty_declined.doAfterTextChanged { text ->
    if (text.toString() != lastKnownDeclineText) {
        lastKnownDeclineText = text.toString()
        if (!text.isNullOrBlank()) {
            if (text.toString().toDouble() > 0) {
                percent_delivered.text = calculatePercentage(selectedProduct)
                decline_reason_layout.visible()
                if (viewModel.validateQty(
                        selectedProduct?.quantity,
                        text.toString().toDouble(),
                        qty_accepted.text.toString().toDouble()
                    )
                ) {
                    selectedProduct?.qtyDeclined =
                        qty_declined.text.toString().toDouble()
                    qty_declined.error = null
                    qty_accepted.error = null
                    percent_delivered.text = calculatePercentage(selectedProduct)
                } else {
                    qty_declined.error =
                        getString(R.string.qty_validation).replacePlaceHolderWithText(
                            StringsPlaceHolders.QTY_VALIDATION,
                            "${selectedProduct?.quantity}"
                        )
                }
            } else {
                decline_reason_layout.gone()
                qty_declined.setText("0")
            }
        } else {
            decline_reason_layout.gone()
            qty_declined.setText("0")
        }
    }
}


temp_et.doAfterTextChanged { text ->
    if (text.isNullOrBlank()) {
        temp_et.setText("0")
    }
}*/
        et_accepted.doAfterTextChanged { text ->
            validateAndUpdate(selectedProduct!!)
        }
        et_returned.doAfterTextChanged { text ->
            decline_reason_layout.visible(!text.isNullOrBlank() && text.toString().toDouble() > 0)
            validateAndUpdate(selectedProduct!!)
        }
        decline_reason.text = RECEIVE_MODELS.declineReasons.firstOrNull() ?: ""
        decline_reason.setOnClickListener {
            decline_reason.showSelectPopUp(RECEIVE_MODELS.declineReasons,
                wrapWidth = true,
                onItemClick = { _, textSelected, _ ->
                    decline_reason.text = textSelected
                    selectedProduct?.declinedReason = textSelected
                })
        }

        receive_btn.setOnSingleClickListener {
            if (et_accepted.text.isNullOrBlank()) {
                et_accepted.setText("0")
            }
            if (et_returned.text.isNullOrBlank()) {
                et_returned.setText("0")
            }
            if (temp_et.text.isNullOrBlank()) {
                temp_et.setText("0")
            }
            val qtyAccepted = et_accepted.text.toString().toDouble()
            val qtyDeclined = et_returned.text.toString().toDouble()

            var qtyPending = 0.0
            selectedProduct?.apply {
                // Apply values to product
                qtyPendingBackup?.let {
                    qtyPending = it - (qtyAccepted + qtyDeclined)
                    if (qtyPending < 0) {
                        // Typically, this case should be filtered when user put the large amount than pending amount on validation phase.
                        // However, we set it as zero if the new calculated pending amount is less than zero.
                        qtyPending = 0.0
                    }
                }

                isReceived = true
                temperature = temp_et.text.toString()
                this.qtyAccepted = qtyAccepted
                this.qtyDeclined = qtyDeclined
                this.qtyPending = qtyPending

                if (this.qtyDeclined != null && this.qtyDeclined!! > 0 && this.declinedReason?.isBlank() == true) { // didn't click on rejected reasons drop down. [AMR 21/4/2021]
                    selectedProduct.declinedReason = RECEIVE_MODELS.declineReasons.first()
                }
            }

            RECEIVE_MODELS.invoiceModelView?.products?.filter { it.productId == selectedProduct?.productId }
                ?.map {
                    it.copy(selectedProduct)
                }
            onReceiveClicked(selectedProduct?.productId ?: "")
            dismiss()
        }

        validateAndUpdate(selectedProduct!!)

        et_accepted.setText(String.format("%.0f", selectedProduct.qtyAccepted))
        et_returned.setText(String.format("%.0f", selectedProduct.qtyDeclined))
    }

    @SuppressLint("SetTextI18n")
    private fun validateAndUpdate(product: ProductModelView) {
        val qtyAccepted = if (!et_accepted.text.isNullOrBlank()) et_accepted.text.toString().toDouble() else 0.0
        val qtyDeclined = if (!et_returned.text.isNullOrBlank()) et_returned.text.toString().toDouble() else 0.0
        val qtyPending = product.qtyPendingBackup!! - (qtyAccepted + qtyDeclined)

        qty_delivered.text = String.format("%.0f %s", qtyAccepted, product.units)
        qty_returned.text = String.format("%.0f %s", qtyDeclined, product.units)
        qty_pending.text = String.format("%.0f %s", qtyPending, product.units)
        et_pending.text = String.format("%.0f", qtyPending)
        qty_pending_warning.visibility = if (qtyPending < 0) View.VISIBLE else View.GONE

        val deliveredPercent = 100 - (qtyPending / product.quantity!!) * 100.0
        percent_delivered.text = String.format("%.1f%%", deliveredPercent)

        var canBeEnabled = qtyPending >= 0 && !et_accepted.text.isNullOrBlank() && !et_returned.text.isNullOrBlank()
        if (!et_returned.text.isNullOrBlank()) {
            canBeEnabled = canBeEnabled && !decline_reason.text.isNullOrBlank()
        }

        if (qtyAccepted == 0.0 && qtyDeclined == 0.0)
            canBeEnabled = false

        receive_btn.isEnabled = canBeEnabled
    }

    private fun calculatePercentage(qtyAccepted: Double, qtyDeclined: Double, qtyPending: Double): Double {
        val total = qtyAccepted + qtyDeclined
        return if (total > 0)
            total / qtyPending * 100
        else
            0.0
    }
}