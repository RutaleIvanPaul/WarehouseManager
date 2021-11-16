package io.ramani.ramaniWarehouse.data.common.network

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

/**
 * Created by Amr on 10/12/17.
 */
class RxErrorHandlingCallAdapterFactory private constructor(private val networkErrorHandler: NetworkErrorHandler) : CallAdapter.Factory() {
    companion object {
        fun create(networkErrorHandler: NetworkErrorHandler) = RxErrorHandlingCallAdapterFactory(networkErrorHandler)
    }

    private val original = RxJava2CallAdapterFactory.create()

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? =
            RxCallAdapterWrapper(original.get(returnType!!, annotations!!, retrofit!!) as CallAdapter<Any, Any>, networkErrorHandler)


    private class RxCallAdapterWrapper<R>(private val wrapped: CallAdapter<R, Any>,
                                          private val networkErrorHandler: NetworkErrorHandler) : CallAdapter<R, Any> {

        override fun adapt(call: Call<R>?): Any {
            val adaptedCall = wrapped.adapt(call!!)

            return hookErrorHandler(adaptedCall)
        }

        override fun responseType(): Type = wrapped.responseType()

        private fun hookErrorHandler(call: Any): Any =
                when (call) {
                    is Observable<*> -> hookErrorHandler(call)
                    is Flowable<*> -> hookErrorHandler(call)
                    is Single<*> -> hookErrorHandler(call)
                    is Completable -> hookErrorHandler(call)
                    else -> call
                }

        private fun hookErrorHandler(observable: Observable<*>): Any =
                observable.onErrorResumeNext { throwable: Throwable -> Observable.error(networkErrorHandler.handleError(throwable)) }

        private fun hookErrorHandler(flowable: Flowable<*>): Any =
                flowable.onErrorResumeNext { throwable: Throwable -> Flowable.error(networkErrorHandler.handleError(throwable)) }

        private fun hookErrorHandler(single: Single<*>): Any =
                single.onErrorResumeNext { Single.error(networkErrorHandler.handleError(it)) }

        private fun hookErrorHandler(completable: Completable): Any =
                completable.onErrorResumeNext { Completable.error(networkErrorHandler.handleError(it)) }
    }
}