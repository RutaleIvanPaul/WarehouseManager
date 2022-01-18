package io.ramani.ramaniWarehouse.domain.returnStock

import io.ramani.ramaniWarehouse.data.entities.BaseResponse
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableStockReturnedListItem
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItems
import io.ramani.ramaniWarehouse.data.returnStock.model.PostReturnItemsResponse
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.reactivex.Single
import retrofit2.http.Body

interface ReturnStockDataSource {
    fun getSalespeople(companyId:String): Single<List<SalespeopleModel>>
    fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockReturnedListItem>>
    fun postReturnedStock(postReturnItems: PostReturnItems): Single<PostReturnItemsResponse>
}