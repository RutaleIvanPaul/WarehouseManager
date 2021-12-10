package io.ramani.ramaniWarehouse.data.stockassignment

import io.ramani.ramaniWarehouse.domain.stockassignment.AssignStockDataSource
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.reactivex.Single

class AssignStockRepository(
    private val assignStockRemoteDataSource: AssignStockRemoteDataSource
    ): AssignStockDataSource {
    override fun getSalesPerson(companyId: String): Single<List<SalesPersonModel>>  =
        assignStockRemoteDataSource.getSalesPerson(companyId)
}