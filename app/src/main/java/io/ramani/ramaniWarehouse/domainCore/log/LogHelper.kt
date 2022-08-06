package io.ramani.ramaniWarehouse.domainCore.log

import android.content.Context
import android.util.Log
import android.widget.Toast
import io.ramani.ramaniWarehouse.BuildConfig

/**
 * log a verbose message
 *
 * @param message the message to print
 */
fun Any.logVerbose(message: String) {
    if (BuildConfig.DEBUG) {
        Log.v(this.javaClass.simpleName, message)
    }
}

/**
 * log an info message
 *
 * @param message the message to print
 */
fun Any.logInfo(message: String) {
    if (BuildConfig.DEBUG) {
        Log.i(this.javaClass.simpleName, message)
    }
}

/**
 * log a debug message
 *
 * @param message the message to print
 */
fun Any.logDebug(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(this.javaClass.simpleName, message)
    }
}

/**
 * log a warning message
 *
 * @param message the message to print
 */
fun Any.logWarning(message: String) {
    if (BuildConfig.DEBUG) {
        Log.w(this.javaClass.simpleName, message)
    }
}

/**
 * log an error message
 *
 * @param message the message to print
 * @param throwable the [Throwable] that occurred (optional parameter)
 */
fun Any.logError(message: String, throwable: Throwable? = null) {
    if (BuildConfig.DEBUG) {
        val tag = this.javaClass.simpleName
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }
}

/**
 * show a short toast
 *
 * @param message the message to display
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * show a short toast in debug mode only
 *
 * @param message the message to display
 */
fun Context.showDebugToast(message: String) {
    if (BuildConfig.DEBUG) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

/**
 * show a long toast
 *
 * @param message the message to display
 */
fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * show a long toast in debug mode only
 *
 * @param message the message to display
 */
fun Context.showLongDebugToast(message: String) {
    if (BuildConfig.DEBUG) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
