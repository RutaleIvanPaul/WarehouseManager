package io.ramani.ramaniWarehouse.data.stockassignment

import io.ramani.ramaniWarehouse.data.stockassignment.model.*
import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.reactivex.Single

class AssignStockRepository(
    private val assignStockRemoteDataSource: AssignStockDataSource
    ): AssignStockDataSource {
    override fun getSalesPerson(companyId: String): Single<List<SalesPersonModel>>  =
        assignStockRemoteDataSource.getSalesPerson(companyId)

    override fun getProducts(companyId: String): Single<List<ProductEntity>> =
        assignStockRemoteDataSource.getProducts(companyId)

    override fun postAssignedStock(postAssignedItems: AssignProductsRequestModel): Single<PostAssignedItemsResponse> =
        assignStockRemoteDataSource.postAssignedStock(postAssignedItems)

}