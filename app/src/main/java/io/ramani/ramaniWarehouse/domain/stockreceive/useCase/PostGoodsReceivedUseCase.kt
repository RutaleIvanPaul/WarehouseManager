package io.ramani.ramaniWarehouse.domain.auth.useCase

import io.ramani.ramaniWarehouse.data.auth.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.domain.auth.StockReceiveDataSource
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.reactivex.Single

class PostGoodsReceivedUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val stockReceiveDataSource: StockReceiveDataSource
) : BaseSingleUseCase<GoodsReceivedModel, GoodsReceivedRequestModel>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: GoodsReceivedRequestModel?): Single<GoodsReceivedModel> =
        stockReceiveDataSource.postGoodsReceived(
            params?.invoiceId!!,
            params.warehouseManagerId,
            params.warehouseId,
            params.distributorId,
            params.date,
            params.time,
            params.deliveryPersonName
        )

}