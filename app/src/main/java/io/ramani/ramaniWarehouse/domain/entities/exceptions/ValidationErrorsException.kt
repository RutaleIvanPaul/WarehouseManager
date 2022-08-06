package io.ramani.ramaniWarehouse.domain.entities.exceptions

import android.util.Log
import io.ramani.ramaniWarehouse.domain.entities.ValidationErrorRemote

/**
 * Created by Amr on 11/22/17.
 */
class ValidationErrorsException(message: String? = "", val validationErrors: List<ValidationErrorRemote>) : Exception(message) {
    fun getErrorMessage(): String {
        var message = ""
        var validationErrorsException = this
        if (validationErrorsException.validationErrors.isNotEmpty()) {
            for (error in validationErrorsException.validationErrors) {
                message = error.errorMsg
                Log.e("message", "" + error.errorMsg)
            }
        }
        Log.e("all message", "" + message)
        return message
    }
}