package io.ramani.ramaniWarehouse.app.common.presentation.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setArgs
import java.util.*

/**
 * Created by Amr on 10/28/17.i
 */
class TimeDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    companion object {
        const val ARG_DEFAULT_DATE = "default_date"

        fun newInstance(date: Long = -1, onTimeSelected: (SelectedTime) -> Unit) =
                TimeDialogFragment().apply {
                    setArgs(ARG_DEFAULT_DATE to date)
                    this.onTimeSelected = onTimeSelected
                }
    }

    private var onTimeSelected: (SelectedTime) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val defaultDate = arguments?.getLong(ARG_DEFAULT_DATE) ?: -1
        if (defaultDate > -1) {
            calendar.timeInMillis = defaultDate
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, false)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val timeInMillis = calendar.timeInMillis

        onTimeSelected(SelectedTime(hourOfDay, minute, timeInMillis))
    }

    data class SelectedTime(val hourOfDay: Int, val minute: Int, val timeInMillis: Long)
}