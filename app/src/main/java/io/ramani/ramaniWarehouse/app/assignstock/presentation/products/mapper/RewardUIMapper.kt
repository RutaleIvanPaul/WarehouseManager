package io.ramani.ramaniWarehouse.app.assignstock.presentation.products.mapper

import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.RewardUIModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.Reward
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.model.RewardEntity

class RewardUIMapper(): ModelMapper<RewardEntity, RewardUIModel> {
    override fun mapFrom(from: RewardEntity): RewardUIModel {
        return RewardUIModel(
            from._id,
            from.createdBy,
            from.dateCreated,
            from.isActive,
            from.maxValue,
            from.name,
            from.rewardType,
            from.rewardUnitPrice,
            from.rewardUnits,
            from.rewardValue,
            from.triggerType,
            from.triggerUnits,
            from.triggerValue
        )
    }

    override fun mapTo(to: RewardUIModel): RewardEntity {
        return RewardEntity(
            to._id,
            to.createdBy,
            to.dateCreated,
            to.isActive,
            to.maxValue,
            to.name,
            to.rewardType,
            to.rewardUnitPrice,
            to.rewardUnits,
            to.rewardValue,
            to.triggerType,
            to.triggerUnits,
            to.triggerValue
        )

    }
}