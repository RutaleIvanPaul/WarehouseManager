package io.ramani.ramaniWarehouse.data.common.source.remote

import android.graphics.Bitmap
import com.google.gson.JsonSyntaxException
import io.ramani.ramaniWarehouse.app.common.io.toFile
import io.ramani.ramaniWarehouse.data.common.network.ErrorConstants
import io.ramani.ramaniWarehouse.data.common.network.toErrorResponseModel
import io.ramani.ramaniWarehouse.domain.base.exceptions.ItemNotFoundException
import io.ramani.ramaniWarehouse.domain.entities.BaseErrorResponse
import io.ramani.ramaniWarehouse.domain.entities.ValidationErrorsResponse
import io.ramani.ramaniWarehouse.domain.entities.exceptions.*
import io.ramani.ramaniWarehouse.domainCore.log.logError
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream

/**
 * Created by Amr on 12/30/17.
 */
abstract class BaseRemoteDataSource {
    companion object {
        private const val SERVER_ERROR = "An error has occurred, please try again"
    }

    fun <T> callSingle(single: Single<T>, onError: (Throwable) -> Boolean = { false },
                       handleError: (Throwable) -> Single<T> = { Single.error(it) }) =
            single.onErrorResumeNext {
                if (onError(it)) {
                    handleError(it)
                } else {
                    handleErrorInternalSingle(it)
                }
            }

    fun callCompletable(completable: Completable, onError: (Throwable) -> Boolean = { false },
                        handleError: (Throwable) -> Completable = { Completable.error(it) }) =
            completable.onErrorResumeNext {
                if (onError(it)) {
                    handleError(it)
                } else {
                    handleErrorInternalCompletable(it)
                }
            }

    private fun <T> handleErrorInternalSingle(throwable: Throwable): Single<T> =
            Single.error(handleErrorInternal(throwable))

    private fun handleErrorInternalCompletable(throwable: Throwable): Completable =
            Completable.error(handleErrorInternal(throwable))

    private fun handleErrorInternal(throwable: Throwable): Throwable {
        val error: Throwable

        error = if (throwable is HttpException) {
            val code = throwable.code()

            when (code) {
                ErrorConstants.NOT_AUTHORIZED_403 -> NotAuthorizedException(getErrorMessage(throwable))
                ErrorConstants.NOT_FOUND_404 -> ItemNotFoundException(getErrorMessage(throwable))
                ErrorConstants.INPUT_VALIDATION_400 -> getValidationError(throwable)?.let {
                    ValidationErrorsException(it.message ?: "", it.data ?: emptyList())
                } ?: ValidationErrorsException("", emptyList())
                else -> throwable
            }
        } else {
            logError("Error Message", throwable)
            when (throwable) {
                is JsonSyntaxException,
                is ParseResponseException -> UnknownNetworkError()
                else -> throwable
            }
        }

        return error
    }

    private fun getErrorMessage(error: HttpException) =
            error.toErrorResponseModel<BaseErrorResponse<Any>>()?.message ?: SERVER_ERROR

    private fun getValidationError(error: HttpException) =
            error.toErrorResponseModel<ValidationErrorsResponse>()

    fun createTextFormData(value:String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

    fun createImageFormData(bitmap: Bitmap): RequestBody {
        return RequestBody.create(MediaType.parse("image/jpg"), bitmap.toFile())
    }
}