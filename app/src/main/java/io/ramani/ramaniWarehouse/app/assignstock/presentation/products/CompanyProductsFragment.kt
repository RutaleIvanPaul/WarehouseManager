package io.ramani.ramaniWarehouse.app.assignstock.presentation.products

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.mapper.ProductUIMapper
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
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
    private val companyProductsList = mutableListOf<ProductsUIModel>()
    private val selectedCompanyProductsList = mutableListOf<ProductsUIModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()
        companyProductsUIModelAdapter = CompanyProductsUIModelAdapter(companyProductsList, onItemClick = {

//            Log.e("222222","item clicked")
//            Log.e("222222",it.isAssigned.toString())
            showDialog(it)
//            viewModel.companyProductsListLiveData.observeForever({
//                companyProductsUIModelAdapter.notifyDataSetChanged()
//            })
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

        CompanyProductsViewmodel.numberOfAssignedProductsLiveData.observe(this, {
            total_assigned_products.setText("${CompanyProductsViewmodel.numberOfAssignedProductsLiveData.value.toString()} Assigned")

        })
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
                primaryUnits.background =
                    ContextCompat.getDrawable(context, R.drawable.round_grey_outline_edit)
            })

            primaryUnits.setOnClickListener(View.OnClickListener {
                primaryUnits.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.round_white_outline_with_no_borders
                )
                secondaryUnits.background =
                    ContextCompat.getDrawable(context, R.drawable.round_grey_outline_edit)
            })
        }
        productImage.apply { loadImage(item.imagePath) }
        assignProductButton.setOnClickListener(View.OnClickListener {
//            viewModel.companyProductsListOriginal?.find { it._id == item._id }?.isAssigned = true
            selectedCompanyProductsList?.find { it._id == item._id }?.isAssigned = true
            selectedCompanyProductsList?.find { it._id == item._id }?.assignedNumber = assignmentQuantity.text.toString().toInt()
            viewModel.companyProductsListOriginal?.find { it._id == item._id }?.assignedNumber = assignmentQuantity.text.toString().toInt()

            viewModel.notifyLiveDataOfAssignmentChange(item._id, assignmentQuantity.text.toString().toInt())
            selectedCompanyProductsList.add(item)
            viewModel.saveAllAssignedProducts(selectedCompanyProductsList)
            companyProductsUIModelAdapter.notifyDataSetChanged()

            dialog.dismiss()
        })
        dialog.show()

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