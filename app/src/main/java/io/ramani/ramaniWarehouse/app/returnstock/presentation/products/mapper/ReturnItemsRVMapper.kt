package io.ramani.ramaniWarehouse.app.returnstock.presentation.products.mapper

import io.ramani.ramaniWarehouse.app.returnstock.presentation.products.model.ReturnItemsRVModel
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableStockReturnedListItem
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper

class ReturnItemsRVMapper: ModelMapper<AvailableStockReturnedListItem,ReturnItemsRVModel> {
    override fun mapFrom(from: AvailableStockReturnedListItem): ReturnItemsRVModel =
        ReturnItemsRVModel(from.products)

    override fun mapTo(to: ReturnItemsRVModel): AvailableStockReturnedListItem {
        TODO("Not yet implemented")
    }
}