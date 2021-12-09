package io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.mapper

import io.ramani.ramaniWarehouse.app.returnstock.presentation.salesperson.model.SalespersonRVModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel

class SalespersonRVMapper:ModelMapper<SalespeopleModel,SalespersonRVModel> {
    override fun mapFrom(from: SalespeopleModel): SalespersonRVModel  =
        SalespersonRVModel(from.id,from.name,false)

    override fun mapTo(to: SalespersonRVModel): SalespeopleModel {
        TODO("Not yet implemented")
    }

}