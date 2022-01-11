package io.ramani.ramaniWarehouse.app.returnstock.presentation.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.retryErrorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.data.returnStock.model.AvailableProductItem
import kotlinx.android.synthetic.main.fragment_select_return_items.*
import org.kodein.di.generic.factory


class SelectReturnItemsFragment : BaseFragment() {
    private lateinit var returnItemsRVAdapter: ReturnItemsRVAdapter
    private val viewModelProvider: (Fragment) -> SelectReturnItemsViewmodel by factory()
    private lateinit var viewModel: SelectReturnItemsViewmodel
    override val baseViewModel: BaseViewModel?
        get() = viewModel
    private val returnItemsRVList = mutableListOf<AvailableProductItem>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        returnItemsRVAdapter = ReturnItemsRVAdapter(returnItemsRVList, onItemClick = {})
    }

    override fun initView(view: View?) {
        super.initView(view)
        return_items_RV.layoutManager = LinearLayoutManager(requireContext())
        return_items_RV.adapter = returnItemsRVAdapter
        subscribeObservers()

        return_items_searchbar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean  = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()){
                    viewModel.availableProductsListLiveData.postValue(viewModel.avaialableProductsListOriginal)
                }else {
                    viewModel.availableProductsListLiveData.postValue(viewModel.avaialableProductsListOriginal.filter {
                        it.productName.contains(
                            newText,
                            ignoreCase = true
                        )
                    } as MutableList<AvailableProductItem>)
                }

                return true
            }

        })

    }

    private fun subscribeObservers() {
        viewModel.availableProductsListLiveData.observe(this,{
            returnItemsRVList.clear()
            returnItemsRVList.addAll(it)
            returnItemsRVAdapter.notifyDataSetChanged()
        })

        SelectReturnItemsViewmodel.missingValueLiveData.observe(this,{
            if(it) {
                errorDialog(getString(R.string.missing_quantity))
                SelectReturnItemsViewmodel.missingValueLiveData.postValue(false)
            }
        })

        observerError(viewModel, this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAvaialableStock()
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun showErrorWithRetry(error: String) {
        super.showErrorWithRetry(error)
        retryErrorDialog(error){
            ReturnStockViewModel.pushBackToStart.postValue(true)
        }
    }

    override fun getLayoutResId() = R.layout.fragment_select_return_items

    companion object {
        @JvmStatic
        fun newInstance() = SelectReturnItemsFragment()

    }
}