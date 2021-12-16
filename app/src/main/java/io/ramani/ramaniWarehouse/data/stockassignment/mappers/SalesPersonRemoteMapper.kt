package io.ramani.ramaniWarehouse.data.stockassignment.mappers

import io.ramani.ramaniWarehouse.data.returnStock.model.SalespeopleRemoteModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.SalesPersonRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel

class SalesPersonRemoteMapper():ModelMapper<SalesPersonRemoteModel, SalesPersonModel> {
    override fun mapFrom(from: SalesPersonRemoteModel): SalesPersonModel {
        return SalesPersonModel.Builder()
            .companyId(from.companyId)
            .id(from.id)
            .isActive(from.isActive)
            .isAdmin(from.isAdmin)
            .name(from.name)
            .phoneNumber(from.phoneNumber)
            .userType(from.userType)
            .build()
    }

    override fun mapTo(to: SalesPersonModel): SalesPersonRemoteModel {
        return SalesPersonRemoteModel(
            to.companyId,
            to.id,
            to.isActive,
            to.isAdmin,
            to.name,
            to.phoneNumber,
            to.userType
        )
    }

}