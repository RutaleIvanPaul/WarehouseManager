package io.ramani.ramaniWarehouse.app.common.presentation.dialogs

import androidx.appcompat.app.AlertDialog
import io.ramani.ramaniWarehouse.R

/**
 * Created by Amr on 9/12/17.
 */
class ErrorDialog(errorMessage: String = "") : BaseErrorDialog(errorMessage) {


    var onCloseClick: () -> Unit = {}

    override fun setButtons(builder: AlertDialog.Builder) {
        builder.setPositiveButton(R.string.close) { _, _ ->
            onCloseClick()
        }
    }


}