package io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.mapper

import io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.model.SalesPersonRVModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.model.SalespersonRVModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel

class SalesPersonRVMapper:ModelMapper<SalesPersonModel, SalesPersonRVModel> {
    override fun mapFrom(from: SalesPersonModel): SalesPersonRVModel  =
        SalesPersonRVModel(from.id,from.name,false)

    override fun mapTo(to: SalesPersonRVModel): SalesPersonModel {
        TODO("Not yet implemented")
    }

}