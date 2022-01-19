package io.ramani.ramaniWarehouse.data.stockassignment.mappers

import io.ramani.ramaniWarehouse.data.stockassignment.model.ProductCategory
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.Reward
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductCategoryEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.RewardEntity

class ProductRemoteMapper(
    private val productCategoryMapper: ModelMapper<ProductCategory, ProductCategoryEntity>,
    private val productRewardsMapper: ModelMapper<Reward, RewardEntity>
) : ModelMapper<RemoteProductModel, ProductEntity> {
    override fun mapFrom(from: RemoteProductModel): ProductEntity {
        val productsCategories: ArrayList<ProductCategoryEntity> = ArrayList()
        val productsRewards: ArrayList<RewardEntity> = ArrayList()
        for (eachProduct in from.productCategories ?: listOf()) {
            productsCategories.add(productCategoryMapper.mapFrom(eachProduct))
        }

        for (eachReward in from.rewards ?: listOf()) {
            productsRewards.add(productRewardsMapper.mapFrom(eachReward))
        }

        return ProductEntity(
            from._id ?: "",
            from.archived ?: false,
            from.commission ?: 0,
            from.currency ?: "",
            from.externalId ?: "",
            from.hasSecondaryUnitConversion ?: false,
            from.imagePath ?: "",
            from.name ?: "",
            productsCategories,
            productsRewards,
            from.secondaryUnitConversion ?: 0,
            from.secondaryUnitName ?: "",
            from.supplierName ?: "",
            from.supplierProductName ?: "",
            from.units ?: "",
            from.vat ?: "",
            from.vatCategory ?: ""
        )
    }

    override fun mapTo(to: ProductEntity): RemoteProductModel {
        val productsCategories: ArrayList<ProductCategory> = ArrayList()
        val productsRewards: ArrayList<Reward> = ArrayList()
        for (eachProduct in to.productCategoryEntities) {
            productsCategories.add(productCategoryMapper.mapTo(eachProduct))
        }

        for (eachReward in to.rewardEntities) {
            productsRewards.add(productRewardsMapper.mapTo(eachReward))
        }
        return RemoteProductModel(
            to._id,
            to.archived,
            to.commission,
            to.currency,
            to.externalId,
            to.hasSecondaryUnitConversion,
            to.imagePath,
            to.name,
            productsCategories,
            productsRewards,
            to.secondaryUnitConversion,
            to.secondaryUnitName,
            to.supplierName,
            to.supplierProductName,
            to.units,
            to.vat,
            to.vatCategory
        )
    }

}