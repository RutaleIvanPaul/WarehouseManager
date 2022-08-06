package io.ramani.ramaniWarehouse.data.stockassignment.mappers

import io.ramani.ramaniWarehouse.data.stockassignment.model.Reward
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.model.RewardEntity

class RemoteRewardMapper(): ModelMapper<Reward, RewardEntity> {
    override fun mapFrom(from: Reward): RewardEntity {
        return RewardEntity(
            from._id?:"",
            from.createdBy?:"",
            from.dateCreated?:"",
            from.isActive?:false,
            from.maxValue?:0,
            from.name?:"",
            from.rewardType?:"",
            from.rewardUnitPrice?:0.0,
            from.rewardUnits?:"",
            from.rewardValue?:0,
            from.triggerType?:"",
            from.triggerUnits?:"",
            from.triggerValue?:0
        )
    }

    override fun mapTo(to: RewardEntity): Reward {
        return Reward(
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