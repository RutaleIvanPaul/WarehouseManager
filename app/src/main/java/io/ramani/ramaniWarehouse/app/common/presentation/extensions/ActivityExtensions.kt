package io.ramani.ramaniWarehouse.app.common.presentation.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import io.ramani.ramaniWarehouse.R
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Amr on 6/19/17.
 */

fun Activity?.isTwoPaneLayout(): Boolean = this?.resources?.getBoolean(R.bool.two_pane_layout) == true

fun Activity.showSnackbar(message: String): Snackbar =
        makeSnackBar(message).apply {
            show()
        }


fun Activity.showSnackbarWithAction(message: String,
                                    actionMessage: String,
                                    onClick: () -> Unit): Snackbar =
        makeSnackBar(message).apply {
            setAction(actionMessage) {
                onClick()
            }
            show()
        }

private fun Activity.makeSnackBar(message: String): Snackbar =
        Snackbar.make(window.decorView.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}