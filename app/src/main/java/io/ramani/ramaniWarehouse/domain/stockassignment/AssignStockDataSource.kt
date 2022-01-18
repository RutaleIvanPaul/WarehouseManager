package io.ramani.ramaniWarehouse.domain.stockassignment

import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.data.stockassignment.model.AllProducts
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItems
import io.ramani.ramaniWarehouse.data.stockassignment.model.PostAssignedItemsResponse
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.reactivex.Single
import okhttp3.RequestBody

interface AssignStockDataSource {
    fun getSalesPerson(companyId: String): Single<List<SalesPersonModel>>
    fun getProducts(companyId: String): Single<List<ProductEntity>>
    fun postAssignedStock(body: RequestBody): Single<PostAssignedItemsResponse>

}