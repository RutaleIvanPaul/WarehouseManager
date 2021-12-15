package io.ramani.ramaniWarehouse.domain.stockassignment

import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import io.reactivex.Single

interface AssignStockDataSource {
    fun getSalesPerson(companyId:String): Single<List<SalesPersonModel>>
}