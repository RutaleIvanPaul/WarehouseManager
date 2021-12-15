package io.ramani.ramaniWarehouse.domain.returnStock

import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableStockReturnedListItem
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.reactivex.Single

interface ReturnStockDataSource {
    fun getSalespeople(companyId:String): Single<List<SalespeopleModel>>
    fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockReturnedListItem>>
}