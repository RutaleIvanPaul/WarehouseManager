package io.ramani.ramaniWarehouse.app.common.presentation.dialogs

import androidx.appcompat.app.AlertDialog
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel

/**
 * Created by Amr on 1/15/18.
 */
class ConfirmDialog : BaseErrorDialog() {
    companion object {
        fun newInstance(errorMessage: String, onConfirmed: () -> Unit = {},
                        onCanceled: () -> Unit = {}, confirmationButtonText: Int = R.string.yes) =
                ConfirmDialog().apply {
                    this.errorMessage = errorMessage
                    this.confirmationMessageText = confirmationButtonText
                    setOnConfirmListener(onConfirmed, onCanceled)
                }
    }

    override val baseViewModel: BaseViewModel?
        get() = null

    private var listener: ConfirmListener? = null

    fun setOnConfirmListener(onConfirmed: () -> Unit = {}, onCanceled: () -> Unit = {}) {
        listener = object : ConfirmListener {
            override fun onConfirm() {
                onConfirmed()
            }

            override fun onCancel() {
                onCanceled()
            }
        }
    }

    override fun setButtons(builder: AlertDialog.Builder) {
        builder.setPositiveButton(confirmationMessageText) { _, _ ->
            onConfirmClick()
        }

        builder.setNegativeButton(R.string.cancel) { _, _ ->
            onCancelClick()
        }
    }

    private fun onConfirmClick() {
        listener?.onConfirm()
    }

    private fun onCancelClick() {
        listener?.onCancel()
    }

    interface ConfirmListener {
        fun onConfirm()

        fun onCancel()
    }
}