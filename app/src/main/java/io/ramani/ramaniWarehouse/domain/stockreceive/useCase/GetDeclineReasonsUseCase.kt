package io.ramani.ramaniWarehouse.domain.auth.useCase

import io.ramani.ramaniWarehouse.domain.auth.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.Params
import io.reactivex.Single

class GetDeclineReasonsUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val stockReceiveDataSource: StockReceiveDataSource
) : BaseSingleUseCase<List<String>, Params>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: Params?): Single<List<String>> =
        stockReceiveDataSource.getDeclineReasons()
}