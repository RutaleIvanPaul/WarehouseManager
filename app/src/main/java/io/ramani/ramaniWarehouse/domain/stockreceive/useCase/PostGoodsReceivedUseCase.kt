package io.ramani.ramaniWarehouse.domain.stockreceive.useCase

import com.google.gson.Gson
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import io.ramani.ramaniWarehouse.data.stockreceive.model.GoodsReceivedRequestModel
import io.ramani.ramaniWarehouse.domain.auth.model.GoodsReceivedModel
import io.ramani.ramaniWarehouse.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.executor.ThreadExecutor
import io.ramani.ramaniWarehouse.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniWarehouse.domain.stockreceive.StockReceiveDataSource
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
            params.deliveryPersonName,
            params.supplierId,
            getItemsFromList(params.items),
            params.storeKeeperSignature,
            params.deliveryPersonSignature

        )

    private fun getItemsFromList(items: List<ProductModelView>?): String? =
        Gson().toJson(items)

}