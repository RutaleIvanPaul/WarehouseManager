package io.ramani.ramaniWarehouse.app.common.presentation.errors

/**
 * Created by Amr on 10/5/17.
 */
data class PresentationError(var error: String, var type: Int) {
    companion object {
        const val ERROR_TEXT = 0
        const val ERROR_TEXT_RETRY = 1
        const val ERROR_TEXT_CONFIRM = 2
    }
}