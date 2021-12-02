package io.ramani.ramaniWarehouse.data.returnStock.mappers

import io.ramani.ramaniWarehouse.data.returnStock.model.SalespeopleRemoteModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.returnStock.model.SalespeopleModel

class SalespeopleRemoteMapper():ModelMapper<SalespeopleRemoteModel,SalespeopleModel> {
    override fun mapFrom(from: SalespeopleRemoteModel): SalespeopleModel {
        return SalespeopleModel.Builder()
            .companyId(from.companyId)
            .isActive(from.isActive)
            .isAdmin(from.isAdmin)
            .name(from.name)
            .phoneNumber(from.phoneNumber)
            .userType(from.userType)
            .build()
    }

    override fun mapTo(to: SalespeopleModel): SalespeopleRemoteModel {
        return SalespeopleRemoteModel(
            to.companyId,
            "",
            to.isActive,
            to.isAdmin,
            to.name,
            to.phoneNumber,
            to.userType
        )
    }

}