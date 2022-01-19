package io.ramani.ramaniWarehouse.domain.stockassignment

import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.data.stockassignment.model.*
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.reactivex.Single
import okhttp3.RequestBody

interface AssignStockDataSource {
    fun getSalesPerson(companyId: String): Single<List<SalesPersonModel>>
    fun getProducts(companyId: String): Single<List<ProductEntity>>
    fun postAssignedStock(body: AssignProductsRequestModel): Single<PostAssignedItemsResponse>

}