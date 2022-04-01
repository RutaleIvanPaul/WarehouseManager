package io.ramani.ramaniWarehouse.app.assignstock.presentation.products.mapper

import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductCategoryUIModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.RewardUIModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductCategoryEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.RewardEntity

class ProductUIMapper(
    private val productCategoryMapper: ModelMapper<ProductCategoryEntity, ProductCategoryUIModel>,
    private val productRewardsMapper: ModelMapper<RewardEntity, RewardUIModel>
) : ModelMapper<ProductEntity, ProductsUIModel> {

    override fun mapFrom(from: ProductEntity): ProductsUIModel {
        val productsCategories: ArrayList<ProductCategoryUIModel> = ArrayList()
        val productsRewards: ArrayList<RewardUIModel> = ArrayList()
        for (eachProduct in from.productCategoryEntities) {
            productsCategories.add(productCategoryMapper.mapFrom(eachProduct))
        }

        for (eachReward in from.rewardEntities) {
            productsRewards.add(productRewardsMapper.mapFrom(eachReward))
        }

        return ProductsUIModel(
            from._id,
            from.archived,
            from.commission,
            from.currency,
            from.externalId,
            from.hasSecondaryUnitConversion,
            from.imagePath,
            from.name,
            productsCategories,
            productsRewards,
            from.secondaryUnitConversion,
            from.secondaryUnitName,
            from.supplierName,
            from.supplierProductName,
            from.units,
            from.vat,
            from.vatCategory,
            supplierProductId= from.supplierProductId
        )
    }

    override fun mapTo(to: ProductsUIModel): ProductEntity {
        TODO("Not yet implemented")
    }
}