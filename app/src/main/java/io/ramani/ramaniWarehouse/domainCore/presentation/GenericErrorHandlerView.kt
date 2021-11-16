package io.ramani.ramaniWarehouse.core.domain.presentation

import io.ramani.ramaniWarehouse.core.domain.exceptions.NotAuthenticatedException
import io.ramani.ramaniWarehouse.core.domain.exceptions.PermissionsChangedException


/**
 * Created by Amr on 10/27/17.
 */
class GenericErrorHandlerView(var onError: (Int, String) -> Unit = { _, _ -> }) : ErrorHandlerView {
    override fun handleError(throwable: Throwable): Boolean = when (throwable) {
        is NotAuthenticatedException -> {
            handleSessionExpired(throwable.message)
            true
        }
        is PermissionsChangedException -> {
            handlePermissionsChanged(throwable.message)
            true
        }
        else -> false
    }

    private fun handleSessionExpired(message: String?) {
        onError(GenericErrors.ERR_SESSION_EXPIRED, message ?: "")
    }

    private fun handlePermissionsChanged(message: String?) {
        onError(GenericErrors.ERR_PERMISSIONS_CHANGED, message ?: "")
    }
}