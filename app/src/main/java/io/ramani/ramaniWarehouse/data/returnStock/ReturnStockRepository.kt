package io.ramani.ramaniWarehouse.data.returnStock

import io.ramani.ramaniWarehouse.domain.returnStock.ReturnStockDataSource
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.reactivex.Single

class ReturnStockRepository(
    private val returnStockRemoteDataSource: ReturnStockRemoteDataSource
    ):ReturnStockDataSource {
    override fun getSalespeople(companyId: String): Single<List<SalespeopleModel>>  =
        returnStockRemoteDataSource.getSalespeople(companyId)
}