package io.ramani.ramaniWarehouse.data.stockassignment.model

data class Reward(
    val _id: String,
    val createdBy: String,
    val dateCreated: String,
    val isActive: Boolean,
    val maxValue: Int,
    val name: String,
    val rewardType: String,
    val rewardUnitPrice: Int,
    val rewardUnits: String,
    val rewardValue: Int,
    val triggerType: String,
    val triggerUnits: String,
    val triggerValue: Int
)