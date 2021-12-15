package io.ramani.ramaniWarehouse.app.returnstock.presentation.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
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
        returnItemsRVAdapter = ReturnItemsRVAdapter(returnItemsRVList){
            Log.d("Available Products",it.productName)
        }
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAvaialableStock()
    }

    override fun getLayoutResId() = R.layout.fragment_select_return_items

    companion object {
        @JvmStatic
        fun newInstance() = SelectReturnItemsFragment()

    }
}