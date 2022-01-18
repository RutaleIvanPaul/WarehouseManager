package io.ramani.ramaniWarehouse.data.returnStock

import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableStockReturnedListItem
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.domain.returnStock.ReturnStockDataSource
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.reactivex.Single

class ReturnStockRepository(
    private val returnStockRemoteDataSource: ReturnStockRemoteDataSource
    ):ReturnStockDataSource {
    override fun getSalespeople(companyId: String): Single<List<SalespeopleModel>>  =
        returnStockRemoteDataSource.getSalespeople(companyId)

    override fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockReturnedListItem>> =
        returnStockRemoteDataSource.getAvailableStock(salesPersonUID)

    override fun postReturnedStock(postReturnItems: PostReturnItems): Single<PostReturnItemsResponse>  =
        returnStockRemoteDataSource.postReturnedStock(postReturnItems)

}