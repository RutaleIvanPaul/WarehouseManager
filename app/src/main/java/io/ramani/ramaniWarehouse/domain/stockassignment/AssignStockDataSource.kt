package io.ramani.ramaniWarehouse.domain.stockassignment

import io.ramani.ramaniWarehouse.data.stockassignment.model.*
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ReportsQueryRequestModel
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ReportsQueryModel
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.reactivex.Single

interface AssignStockDataSource {
    fun getSalesPerson(companyId: String): Single<List<SalesPersonModel>>
    fun getProducts(companyId: String): Single<List<ProductEntity>>
    fun postAssignedStock(body: AssignProductsRequestModel): Single<PostAssignedItemsResponse>
    fun postAssignedWarehouseStock(
       body: PostWarehouseAssignedItems, warehouseId: String
    ): Single<String>

    fun getReportsQuery(body: ReportsQueryRequestModel): Single<ReportsQueryModel>

}