package io.ramani.ramaniWarehouse.app.assignstock.presentation.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.mapper.ProductUIMapper
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.mappers.mapToWith
import kotlinx.android.synthetic.main.fragment_stock_assign_product.*
import org.kodein.di.generic.factory


class CompanyProductsFragment : BaseFragment() {
    private lateinit var companyProductsUIModelAdapter: CompanyProductsUIModelAdapter
    private val viewModelProvider: (Fragment) -> CompanyProductsViewmodel by factory()
    private lateinit var viewModel: CompanyProductsViewmodel
    override val baseViewModel: BaseViewModel?
        get() = viewModel
    private val companyProductsList = mutableListOf<RemoteProductModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        companyProductsUIModelAdapter = CompanyProductsUIModelAdapter(companyProductsList, onItemClick = {})
    }

    override fun initView(view: View?) {
        super.initView(view)
        company_products_RV.layoutManager = GridLayoutManager(requireContext(), 2 )
        company_products_RV.adapter = companyProductsUIModelAdapter
        subscribeObservers()

        company_products_searchbar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean  = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()){
                    viewModel.companyProductsListLiveData.postValue(viewModel.companyProductsListOriginal)
                }else {
                    viewModel.companyProductsListLiveData.postValue(viewModel.companyProductsListOriginal.filter {
                        it.name.contains(
                            newText,
                            ignoreCase = true
                        )
                    } as MutableList<RemoteProductModel>)
                }

                return true
            }

        })

    }

    private fun subscribeObservers() {
        viewModel.companyProductsListLiveData.observe(this,{
            companyProductsList.clear()
            companyProductsList.addAll(it)
            it.forEach {
                companyProductsList.add(it)
            }
            companyProductsUIModelAdapter.notifyDataSetChanged()
        })

        CompanyProductsViewmodel.noProductSelectedLiveData.observe(this,{
            if(it) {
                errorDialog(getString(R.string.missing_product_selection))
                CompanyProductsViewmodel.noProductSelectedLiveData.postValue(false)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCompanyProducts()
    }

    override fun getLayoutResId() = R.layout.fragment_stock_assign_product

    companion object {
        @JvmStatic
        fun newInstance() = CompanyProductsFragment()

    }
}