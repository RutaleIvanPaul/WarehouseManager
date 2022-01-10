package io.ramani.ramaniWarehouse.domain.stockassignment.model

data class RewardEntity(
    val _id: String,
    val createdBy: String,
    val dateCreated: String,
    val isActive: Boolean,
    val maxValue: Int,
    val name: String,
    val rewardType: String,
    val rewardUnitPrice: Double,
    val rewardUnits: String,
    val rewardValue: Int,
    val triggerType: String,
    val triggerUnits: String,
    val triggerValue: Int
)