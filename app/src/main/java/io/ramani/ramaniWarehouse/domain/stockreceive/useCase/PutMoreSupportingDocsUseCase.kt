package io.ramani.ramaniWarehouse.domain.stockreceive.useCase

import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockreceive.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.stockreceive.model.GoodsReceivedModel
import io.reactivex.Single

class PutMoreSupportingDocsUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val stockReceiveDataSource: StockReceiveDataSource
): BaseSingleUseCase<String, GoodsReceivedRequestModel>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: GoodsReceivedRequestModel?): Single<String> =
        stockReceiveDataSource.putMoreSupportingDocs(params!!.body)

}