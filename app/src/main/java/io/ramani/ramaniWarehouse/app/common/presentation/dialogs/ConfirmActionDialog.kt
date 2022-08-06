package io.ramani.ramaniWarehouse.app.common.presentation.dialogs

import androidx.appcompat.app.AlertDialog
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.reactivex.subjects.PublishSubject


/**
 * Created by Amr on 9/12/17.
 */
class ConfirmActionDialog(confirmMessage: String = "") : BaseErrorDialog(confirmMessage) {

    override val baseViewModel: BaseViewModel?
        get() = null

    val onConfirmClickedObservable = PublishSubject.create<Boolean>()
    val onCancelClickedObservable = PublishSubject.create<Boolean>()

    var onConfirmClick: () -> Unit = {
        onConfirmClickedObservable.onNext(true)
    }
    var onCancelClick: () -> Unit = {
        onCancelClickedObservable.onNext(true)
    }


    override fun setButtons(builder: AlertDialog.Builder) {
        builder.setPositiveButton(R.string.yes) { _, _ ->
//            onConfirmClick
            onConfirmClickedObservable.onNext(true)
        }

        builder.setNegativeButton(R.string.cancel) { _, _ ->
            onCancelClickedObservable.onNext(true)
//            onCancelClick
        }
    }
}