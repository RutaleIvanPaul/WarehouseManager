package io.ramani.ramaniWarehouse.data.stockassignment

import io.ramani.ramaniWarehouse.data.stockassignment.model.AllProducts
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItems
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItemsResponse
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.reactivex.Single

class AssignStockRepository(
    private val assignStockRemoteDataSource: AssignStockRemoteDataSource
    ): AssignStockDataSource {
    override fun getSalesPerson(companyId: String): Single<List<SalesPersonModel>>  =
        assignStockRemoteDataSource.getSalesPerson(companyId)

    override fun getProducts(companyId: String): Single<List<ProductEntity>> =
        assignStockRemoteDataSource.getProducts(companyId)

    override fun postAssignedStock(postAssignedItems: PostAssignedItems): Single<PostAssignedItemsResponse> =
        assignStockRemoteDataSource.postAssignedStock(postAssignedItems)

}