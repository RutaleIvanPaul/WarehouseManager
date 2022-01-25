package io.ramani.ramaniWarehouse.app.assignstock.presentation.products

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.model.ASSIGNMENT_RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.mapper.ProductUIMapper
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.data.stockassignment.model.RemoteProductModel
import io.ramani.ramaniWarehouse.domain.base.mappers.mapFromWith
import io.ramani.ramaniWarehouse.domain.base.mappers.mapToWith
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import kotlinx.android.synthetic.main.fragment_assign_stock.*
import kotlinx.android.synthetic.main.fragment_stock_assign_product.*
import org.jetbrains.anko.backgroundDrawable
import org.kodein.di.generic.factory


class CompanyProductsFragment : BaseFragment() {
    private lateinit var companyProductsUIModelAdapter: CompanyProductsUIModelAdapter
    private val viewModelProvider: (Fragment) -> CompanyProductsViewmodel by factory()
    private lateinit var viewModel: CompanyProductsViewmodel
    override val baseViewModel: BaseViewModel?
        get() = viewModel
    private val companyProductsList = mutableListOf<ProductsUIModel>()
    private val selectedCompanyProductsList = mutableListOf<ProductsUIModel>()
    private val assignedItemsList = mutableListOf<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()
        companyProductsUIModelAdapter = CompanyProductsUIModelAdapter(companyProductsList, onItemClick = {

            showDialog(it)

        })
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
                    } as MutableList<ProductsUIModel>)
                }

                return true
            }

        })

    }

    private fun subscribeObservers() {
        viewModel.companyProductsListLiveData.observe(this,{
            companyProductsList.clear()
            companyProductsList.addAll(it)
            companyProductsUIModelAdapter.notifyDataSetChanged()
        })

        CompanyProductsViewmodel.noProductSelectedLiveData.observe(this,{
            if(it) {
                errorDialog(getString(R.string.missing_product_selection))
                CompanyProductsViewmodel.noProductSelectedLiveData.postValue(false)
            }
        })

        viewModel.startLoadingProducts.observeForever {
            it?.apply(::setLoadingIndicatorVisible)
        }

        viewModel.noProducts.observeForever {
            it?.apply({
                no_products_data_tv.visible(true)
            }
            )
        }
    }

    private fun showDialog(item: ProductsUIModel) {
        val context = requireContext()
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_assign_products)
        val body = dialog.findViewById(R.id.layout_custom_rewards_dialogue_box_product_id) as TextView
        val primaryUnits = dialog.findViewById(R.id.dialogue_custom_forms_custom_price_button) as TextView
        val secondaryUnits = dialog.findViewById(R.id.dialogue_custom_forms_qty_button) as TextView
        val assignmentQuantity = dialog.findViewById(R.id.layout_first_edit_text_dialogue_edit_text) as EditText
        val productImage = dialog.findViewById(R.id.price_img) as ImageView
        val assignProductButton = dialog.findViewById(R.id.custom_products_assign_button) as Button
        if(item.assignedNumber != 0) assignmentQuantity.setText(item.assignedNumber.toString())



        body.text = item.name
        primaryUnits.text = item.units
        viewModel.companyProductsListOriginal?.find { it._id == item._id }?.selectedUnits = item.units

        if (!item.hasSecondaryUnitConversion){
            primaryUnits.layoutParams.width = 600
            secondaryUnits.visibility = View.GONE

        }
        else {
            secondaryUnits.setOnClickListener(View.OnClickListener {
                secondaryUnits.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.round_white_outline_with_no_borders
                )
                secondaryUnits.text = item.secondaryUnitName
                viewModel.companyProductsListOriginal?.find { it._id == item._id }?.selectedUnits = item.secondaryUnitName

                primaryUnits.background =
                    ContextCompat.getDrawable(context, R.drawable.round_grey_outline_edit)
            })

            primaryUnits.setOnClickListener(View.OnClickListener {
                primaryUnits.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.round_white_outline_with_no_borders
                )
                viewModel.companyProductsListOriginal?.find { it._id == item._id }?.selectedUnits = item.units

                secondaryUnits.background =
                    ContextCompat.getDrawable(context, R.drawable.round_grey_outline_edit)
            })
        }
        productImage.apply { loadImage(item.imagePath) }
        assignProductButton.setOnClickListener(View.OnClickListener {


            if(assignmentQuantity.text.trim().toString().isNullOrEmpty()){
                Toast.makeText(context, R.string.record_assignemnt_number, Toast.LENGTH_LONG).show()

            }
            else {

                viewModel.companyProductsListOriginal?.find { it._id == item._id }?.isAssigned = true
                viewModel.companyProductsListOriginal?.find { it._id == item._id }?.assignedResource = 1
                viewModel.companyProductsListOriginal?.find { it._id == item._id }?.assignedResourceID = R.drawable.assgn_button
                viewModel.companyProductsListOriginal?.find { it._id == item._id }?.displayText = "Edit"

                viewModel.companyProductsListOriginal?.find { it._id == item._id }?.assignedNumber =
                    assignmentQuantity.text.trim().toString()?.toInt() ?: 0

                viewModel.notifyLiveDataOfAssignmentChange(item)
                selectedCompanyProductsList.add(item)
                viewModel.saveAllAssignedProducts(selectedCompanyProductsList)
                companyProductsUIModelAdapter.notifyDataSetChanged()
                AssignStockViewModel.allowToGoNext.postValue(Pair(1,true))
                total_assigned_products.setText("${viewModel.companyProductsListOriginal?.filter { it.isAssigned == true }.count()} Assigned")



                dialog.dismiss()
            }
        })
        dialog.show()

    }



    override fun onResume() {
        super.onResume()
        viewModel.resetViewModelData()
        viewModel.serverProductsLoaded.observeForever {
            if(it == false){
                viewModel.getCompanyProducts()
            }
        }
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        stock_assigned_products_loader.visible(visible)
    }

    override fun getLayoutResId() = R.layout.fragment_stock_assign_product

    companion object {
        @JvmStatic
        fun newInstance() = CompanyProductsFragment()

    }

}