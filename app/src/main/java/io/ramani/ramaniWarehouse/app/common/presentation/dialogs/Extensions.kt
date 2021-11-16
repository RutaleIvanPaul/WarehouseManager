package io.ramani.ramaniWarehouse.app.common.presentation.dialogs

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.ramani.ramaniWarehouse.core.domain.date.getHour
import io.ramani.ramaniWarehouse.core.domain.date.getMinute
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.Timepoint
import io.ramani.ramaniWarehouse.R
import java.util.*

/**
 * Created by Amr on 11/14/17.
 */
fun Fragment.errorDialog(errorMessage: String, onClosed: () -> Unit = {}) {
    showErrorDialog(errorMessage, onClosed, childFragmentManager)
}

fun FragmentActivity.errorDialog(errorMessage: String, onClosed: () -> Unit = {}) {
    showErrorDialog(errorMessage, onClosed, supportFragmentManager)
}

fun Fragment.retryErrorDialog(errorMessage: String, onRetry: () -> Unit = {}) {
    retryErrorDialog(errorMessage, onRetry, childFragmentManager)
}

fun AppCompatActivity.errorDialog(errorMessage: String, onClosed: () -> Unit = {}) {
    showErrorDialog(errorMessage, onClosed, supportFragmentManager)
}

fun AppCompatActivity.showRetryErrorDialog(errorMessage: String, onRetry: () -> Unit = {}) {
    retryErrorDialog(errorMessage, onRetry, supportFragmentManager)
}

fun Fragment.showConfirmDialog(message: String, onCanceled: () -> Unit = {}, confirmationButtonText: Int = R.string.yes, onConfirmed: () -> Unit) {
    ConfirmDialog.newInstance(message, onConfirmed, onCanceled, confirmationButtonText).show(childFragmentManager, null)
}

fun showErrorDialog(errorMessage: String, onClosed: () -> Unit = {}, childFragmentManager: androidx.fragment.app.FragmentManager) {
    ErrorDialog(errorMessage).apply {
        this.onCloseClick = onClosed

        isCancelable = false
    }.show(childFragmentManager, "error_dialog")
}

fun retryErrorDialog(errorMessage: String, onRetry: () -> Unit = {}, childFragmentManager: androidx.fragment.app.FragmentManager) {
    RetryErrorDialog(errorMessage).apply {
        this.onRetryClick = onRetry
    }.show(childFragmentManager, "retry_error_dialog")
}

fun Fragment.showDatePicker(date: Long = -1, minDate: Long = -1, maxDate: Long = -1, onDateSelected: (Int, Int, Int, Long) -> Unit) {
    if (isAdded) {
        val datePicker = buildDatePicker(date, minDate, maxDate, onDateSelected)
        datePicker.setOkText(R.string.ok_button)
        datePicker.setCancelText(R.string.cancel)
        activity?.apply {
            datePicker.show(this.fragmentManager, "date_picker")
        }
    }
}

fun Fragment.showDatePicker(date: Long = -1, minDate: Long = -1, maxDate: Long = -1, disableSelectedDate: MutableList<Calendar>, onDateSelected: (Int, Int, Int, Long) -> Unit) {
    if (isAdded) {

        val datePicker = buildDatePicker(date, minDate, maxDate, onDateSelected)
        datePicker.setOkText(R.string.ok_button)
        datePicker.setCancelText(R.string.cancel)
        if (!disableSelectedDate.isEmpty())
            datePicker.disabledDays = disableSelectedDate.toTypedArray()
        activity?.apply {
            datePicker.show(this.fragmentManager, "date_picker")
        }
    }
}


fun buildDatePicker(date: Long = -1, minDate: Long = -1, maxDate: Long = -1, onDateSelected: (Int, Int, Int, Long) -> Unit): DatePickerDialog {
    val calendar = Calendar.getInstance()
    if (date > -1) {
        calendar.timeInMillis = date
    }
    val defaultYear = calendar.get(Calendar.YEAR)
    val defaultMonth = calendar.get(Calendar.MONTH)
    val defaultDay = calendar.get(Calendar.DAY_OF_MONTH)

    val datePicker = DatePickerDialog.newInstance({ _, year, monthOfYear, dayOfMonth ->
        val timInMillis = Calendar.getInstance().apply {
            set(year, monthOfYear, dayOfMonth, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        onDateSelected(year, monthOfYear, dayOfMonth, timInMillis)
    }, defaultYear, defaultMonth, defaultDay).apply {
        if (minDate > -1) {
            setMinDate(Calendar.getInstance().apply { timeInMillis = minDate })
        }
        if (maxDate > -1) {
            setMaxDate(Calendar.getInstance().apply { timeInMillis = maxDate })
        }
    }
    return datePicker
}

fun Activity.showDatePicker(date: Long = -1, minDate: Long = -1, onDateSelected: (Int, Int, Int, Long) -> Unit) {
    val calendar = Calendar.getInstance()
    if (date > -1) {
        calendar.timeInMillis = date
    }
    val defaultYear = calendar.get(Calendar.YEAR)
    val defaultMonth = calendar.get(Calendar.MONTH)
    val defaultDay = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog.newInstance({ _, year, monthOfYear, dayOfMonth ->
        val timInMillis = Calendar.getInstance().apply {
            set(year, monthOfYear, dayOfMonth, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        onDateSelected(year, monthOfYear, dayOfMonth, timInMillis)
    }, defaultYear, defaultMonth, defaultDay).apply {
        setOkText(R.string.ok_button)
        setCancelText(R.string.cancel)
        if (minDate > -1) {
            setMinDate(Calendar.getInstance().apply { timeInMillis = minDate })
        }
    }.show(this.fragmentManager, "date_picker")
}

fun Fragment.GetTimePoint(time: Long): Timepoint {
    return Timepoint(time.getHour(), time.getMinute(), 0)
}

fun Fragment.showTimePicker(date: Long = -1, minTime: Long = -1, maxTime: Long = -1, is24Hours: Boolean = false, onTimeSelected: (Int, Int, Long) -> Unit) {
    if (isAdded) {
        val calendar = Calendar.getInstance()
        if (date > -1) {
            calendar.timeInMillis = date
        }

        val defaultHour = calendar.get(Calendar.HOUR_OF_DAY)
        val defaultMinute = calendar.get(Calendar.MINUTE)

        activity?.fragmentManager?.apply {
            TimePickerDialog.newInstance({ _, hour, minute, second ->
                val timeInMillis = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                onTimeSelected(hour, minute, timeInMillis)
            }, defaultHour, defaultMinute, is24Hours).apply {
                setOkText(R.string.ok_button)
                setCancelText(R.string.cancel)

                if (maxTime > minTime) {
                    if (minTime > -1) {
                        setMinTime(GetTimePoint(minTime))
                    }
                    if (maxTime > -1) {
                        setMaxTime(GetTimePoint(maxTime))
                    }
                }

            }.show(this, "time_picker")
        }
    }
}