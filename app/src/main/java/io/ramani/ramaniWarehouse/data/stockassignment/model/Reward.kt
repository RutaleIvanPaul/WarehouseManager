package io.ramani.ramaniWarehouse.data.stockassignment.model

data class Reward(
    val _id: String?="",
    val createdBy: String?="",
    val dateCreated: String?="",
    val isActive: Boolean?=false,
    val maxValue: Int?=0,
    val name: String?="",
    val rewardType: String?="",
    val rewardUnitPrice: Double?=0.0,
    val rewardUnits: String?="",
    val rewardValue: Int?=0,
    val triggerType: String?="",
    val triggerUnits: String?="",
    val triggerValue: Int?=0
)