package io.ramani.ramaniWarehouse.domain.stockassignment

import io.ramani.ramaniWarehouse.data.stockassignment.model.AllProducts
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.reactivex.Single

interface AssignStockDataSource {
    fun getSalesPerson(companyId: String): Single<List<SalesPersonModel>>
    fun getProducts(companyId: String): Single<List<RemoteProductModel>>
}