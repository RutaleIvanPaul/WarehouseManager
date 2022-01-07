package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveStock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.BaseBottomSheetDialogFragment
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.gone
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.showSelectPopUp
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.language.StringsPlaceHolders
import io.ramani.ramaniWarehouse.app.common.presentation.language.replacePlaceHolderWithText
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import kotlinx.android.synthetic.main.fragment_product_confirm_sheet.*
import kotlinx.android.synthetic.main.fragment_signin_sheet.*
import org.kodein.di.generic.factory

const val ARG_PRODUCT_ID = "arg_product_id"

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
        var selectedProduct =
            RECEIVE_MODELS.invoiceModelView?.products?.find { it.productId == productId }
        product_name.text = selectedProduct?.productName
        qty_incoming.text = "${selectedProduct?.quantity} ${selectedProduct?.unit}"
        percent_delivered.text = calculatePercentage(selectedProduct)
        var lastKnownAcceptedText = ""
        qty_accepted.doAfterTextChanged { text ->
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
                            selectedProduct?.quantityAccepted =
                                qty_accepted.text.toString().toDouble()
                            qty_accepted.error = null
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
                            selectedProduct?.quantityDeclined =
                                qty_declined.text.toString().toDouble()
                            qty_declined.error = null
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
        decline_reason.text = RECEIVE_MODELS.declineReasons.firstOrNull() ?: ""
        decline_reason.setOnClickListener {
            decline_reason.showSelectPopUp(RECEIVE_MODELS.declineReasons,
                wrapWidth = true,
                onItemClick = { _, textSelected, _ ->
                    decline_reason.text = textSelected
                    selectedProduct?.declineReason = textSelected
                })
        }
        receive_btn.setOnSingleClickListener {
            selectedProduct?.isReceived = true
            RECEIVE_MODELS.invoiceModelView?.products?.filter { it.productId == selectedProduct?.productId }
                ?.map {
                    it.copy(selectedProduct)
                }
            onReceiveClicked(selectedProduct?.productId ?: "")
            dismiss()
        }
    }

    private fun calculatePercentage(selectedProduct: ProductModelView?): String? {
        val accepted = (selectedProduct?.quantityAccepted ?: 0).toDouble()
        val declined: Double = (selectedProduct?.quantityDeclined ?: 0).toDouble()
        val footer = accepted + declined
        if (footer != 0.0) {
            val percentage =
                accepted / footer * 100
            return "$percentage %"
        }
        return "0"
    }
}