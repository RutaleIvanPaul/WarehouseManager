package io.ramani.ramaniWarehouse.app.common.presentation.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setArgs
import java.util.*

/**
 * Created by Amr on 10/27/17.
 */
class DateDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    companion object {
        private const val ARG_DEFAULT_DATE = "default_date"
        fun newInstance(date: Long = -1, onDateSelected: (SelectedDate) -> Unit) =
                DateDialogFragment().apply {
                    setArgs(ARG_DEFAULT_DATE to date)
                    this.onDateSelected = onDateSelected
                }
    }

    var onDateSelected: (SelectedDate) -> Unit = { }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val defaultDate = arguments?.getLong(ARG_DEFAULT_DATE) ?: -1
        val calendar = Calendar.getInstance()
        if (defaultDate > -1) {
            calendar.timeInMillis = defaultDate
        }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth, 0, 0, 0)
        onDateSelected(SelectedDate(year, month, dayOfMonth, calendar.timeInMillis))
    }

    data class SelectedDate(val year: Int, val month: Int, val dayOfMonth: Int, val timeInMillis: Long)
}