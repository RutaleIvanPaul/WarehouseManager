package io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.assignstock.flow.AssignStockFlow
import io.ramani.ramaniWarehouse.app.assignstock.flow.AssignStockFlowcontroller
import io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm.model.AssignedItemDetails
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.AssignStockViewModel
import io.ramani.ramaniWarehouse.app.assignstock.presentation.host.model.ASSIGNMENT_RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.fragment_confirm_assign_stock.*
import org.kodein.di.generic.factory


class ConfirmAssignedStockFragment : BaseFragment() {
    private lateinit var confirmAssignedItemsAdapter: ConfirmAssignedItemsAdapter
    private val viewModelProvider: (Fragment) -> ConfirmAssignedStockViewModel by factory()
    private lateinit var viewModel: ConfirmAssignedStockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: AssignStockFlow
    private val selectedCompanyProductsList = mutableListOf<ProductsUIModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()

        confirmAssignedItemsAdapter =
//            ConfirmAssignedItemsAdapter(ASSGNMENT_RECEIVE_MODELS.productsSelection.value?.toMutableList()) {}
            ConfirmAssignedItemsAdapter(selectedCompanyProductsList, {})

        ASSIGNMENT_RECEIVE_MODELS.productsSelection.observeForever({
            Log.e("xxxxx Confirm ", it.toString())

            confirmAssignedItemsAdapter.notifyDataSetChanged()
        })


    }

    override fun getLayoutResId() = R.layout.fragment_confirm_assign_stock

    override fun initView(view: View?) {
        super.initView(view)
        confirm_assign_items_RV.layoutManager = LinearLayoutManager(requireContext())
        confirm_assign_items_RV.adapter = confirmAssignedItemsAdapter
        subscribeObservers()

        flow = AssignStockFlowcontroller(baseActivity!!, R.id.main_fragment_container)

        confirm_assign_sign_store_keeper.setOnSingleClickListener {
            flow.openAssignStockSignPad(AssignedStockSignaturePadFragment.PARAM_STORE_KEEPER_SIGN)
        }

        confirm_assign_sign_salesperson.setOnSingleClickListener {
            flow.openAssignStockSignPad(AssignedStockSignaturePadFragment.PARAM_SALESPERSON_SIGN)
        }
    }

    private fun subscribeObservers() {
        viewModel.onItemsAssignedLiveData.observe(this, {
//            onItemsReturned(it)
        })

        ASSIGNMENT_RECEIVE_MODELS.productsSelection.observeForever({
            AssignedItemDetails.assignedItems = it.toMutableList()

            selectedCompanyProductsList.clear()
            selectedCompanyProductsList.addAll(it)
            confirmAssignedItemsAdapter.notifyDataSetChanged()
        })

//        AssignStockViewModel.assignedItemsChangedLiveData.observe(this, {
//            if (it){
//                AssignStockViewModel.allowToGoNext.postValue(Pair(1, true))
//                confirmAssignedItemsAdapter.notifyDataSetChanged()
//            }
//        })

        AssignStockViewModel.signedLiveData.observe(this, {
            if (it.first == AssignedStockSignaturePadFragment.PARAM_STORE_KEEPER_SIGN) {

                confirm_assign_sign_store_keeper.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check_icon
                    ), null, null, null
                )
                confirm_assign_sign_store_keeper.setBackgroundResource(R.drawable.green_stroke_action_button)
                confirm_assign_sign_store_keeper.setText(R.string.signed)
                confirm_assign_sign_store_keeper.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_lime_yellow
                    )
                )

                AssignStockViewModel.assignedItemDetails.signatureInfoStoreKeeper = it.second

            } else if (it.first == AssignedStockSignaturePadFragment.PARAM_SALESPERSON_SIGN) {

                confirm_assign_sign_salesperson.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check_icon
                    ), null, null, null
                )
                confirm_assign_sign_salesperson.setBackgroundResource(R.drawable.green_stroke_action_button)
                confirm_assign_sign_salesperson.setText(R.string.signed)
                confirm_assign_sign_salesperson.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_lime_yellow
                    )
                )

                AssignStockViewModel.assignedItemDetails.signatureInfoSalesPerson = it.second

            }

            if(
                AssignStockViewModel.assignedItemDetails.signatureInfoSalesPerson != null &&
                AssignStockViewModel.assignedItemDetails.signatureInfoStoreKeeper != null
            ){
                AssignStockViewModel.allowToGoNext.postValue(Pair(2,true))
            }
        })

    }

    override fun onResume() {
        super.onResume()
        confirm_assign_store_keeper_name.text =
            AssignStockViewModel.assignedItemDetails.storekeeperName
        confirm_assign_salesperson_name.text =
            AssignStockViewModel.assignedItemDetails.salespersonName
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ConfirmAssignedStockFragment()
    }

}