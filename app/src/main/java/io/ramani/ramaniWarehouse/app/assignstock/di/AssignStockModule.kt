package io.ramani.ramaniWarehouse.app.assignstock.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniWarehouse.app.assignstock.presentation.AssignStockSalesPersonViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.mapper.SalesPersonRVMapper
import io.ramani.ramaniWarehouse.app.assignstock.presentation.assignstocksalesperson.model.SalesPersonRVModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.ConfirmAssignedStockViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.CompanyProductsViewmodel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.mapper.ProdcutCategoryUIMapper
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.mapper.ProductUIMapper
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.mapper.RewardUIMapper
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductCategoryUIModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.RewardUIModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.AssignSuccessViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.mappers.AssignedItemsMapper
import io.ramani.ramaniWarehouse.data.stockassignment.model.ConfirmProducts
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductCategoryEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.ProductEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.RewardEntity
import io.ramani.ramaniWarehouse.domain.stockassignment.model.SalesPersonModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val assignStockModule = Kodein.Module("assignStockModule") {

    import(assignStockDataModule)
    import(assignStockDomainModule)

    bind<AssignStockViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, AssignStockViewModel.Factory(
                instance(), instance(), instance(), instance("postAssignedStockUseCase"), instance(), instance(), instance()
            )
        ).get(AssignStockViewModel::class.java)
    }

    bind<AssignStockSalesPersonViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, AssignStockSalesPersonViewModel.Factory(
                instance(),
                instance(),
                instance(),
                instance("getSalesPersonUseCase"),
                instance(),
                instance()
            )
        ).get(AssignStockSalesPersonViewModel::class.java)
    }

    bind<ModelMapper<SalesPersonModel, SalesPersonRVModel>>() with provider {
        SalesPersonRVMapper()
    }

    bind<CompanyProductsViewmodel>() with factory{ fragment: Fragment ->
        ViewModelProvider(
            fragment, CompanyProductsViewmodel.Factory(
                instance(), instance(), instance(),
                instance("getCompanyProductsUseCase"),
                instance(), instance()
            )
        ).get(CompanyProductsViewmodel::class.java)

    }

    bind<ModelMapper<ProductEntity, ProductsUIModel>>() with provider {
        ProductUIMapper(instance(), instance())
    }

    bind<ModelMapper<ProductCategoryEntity, ProductCategoryUIModel>>() with provider {
        ProdcutCategoryUIMapper()
    }

    bind<ModelMapper<RewardEntity, RewardUIModel>>() with provider {
        RewardUIMapper()
    }

    bind<ModelMapper<ProductsUIModel, ConfirmProducts>>() with provider {
        AssignedItemsMapper()
    }

    bind<ConfirmAssignedStockViewModel>() with factory{ fragment: Fragment ->
        ViewModelProvider(
            fragment, ConfirmAssignedStockViewModel.Factory(
                instance(), instance(), instance(), instance("postAssignedStockUseCase")
                , instance(), instance(), instance()
            )
        ).get(ConfirmAssignedStockViewModel::class.java)

    }

    bind<AssignSuccessViewModel>() with factory{ fragment: Fragment ->
        ViewModelProvider(
            fragment, AssignSuccessViewModel.Factory(
                instance(), instance(), instance()
            )
        ).get(AssignSuccessViewModel::class.java)

    }
}