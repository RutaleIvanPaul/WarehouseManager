package io.ramani.ramaniWarehouse.app.assignstock.presentation.confirm

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import io.ramani.ramaniWarehouse.app.common.io.toFile
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
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

    val storeKeeperTextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (stock_assign_confirm_other_storekeeper_name.text.isNullOrEmpty()){
                AssignStockViewModel.allowToGoNext.postValue(Pair(2, false))
            }else{
                AssignedItemDetails.assignedToWarehouseStoreKeeperName = stock_assign_confirm_other_storekeeper_name.text.toString()
                AssignStockViewModel.allowToGoNext.postValue(Pair(2, true))
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()

        confirmAssignedItemsAdapter =
//            ConfirmAssignedItemsAdapter(ASSGNMENT_RECEIVE_MODELS.productsSelection.value?.toMutableList()) {}
            ConfirmAssignedItemsAdapter(selectedCompanyProductsList, {})

        ASSIGNMENT_RECEIVE_MODELS.productsSelection.observeForever {
            if (!it.isNullOrEmpty()) AssignStockViewModel.assignedItemsChangedLiveData.postValue(
                true
            )

            confirmAssignedItemsAdapter.notifyDataSetChanged()
        }


        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel,this)
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
        stock_assign_confirm_other_storekeeper_name.addTextChangedListener(storeKeeperTextWatcher)
    }

    private fun subscribeObservers() {
        viewModel.onItemsAssignedLiveData.observe(this, {
//            onItemsReturned(it)
        })

        ASSIGNMENT_RECEIVE_MODELS.productsSelection.observeForever {
            AssignedItemDetails.assignedItems = it.distinct().toMutableList()

            selectedCompanyProductsList.clear()
            selectedCompanyProductsList.addAll(it.distinct())
            confirmAssignedItemsAdapter.notifyDataSetChanged()
        }

        AssignStockViewModel.assignedItemsChangedLiveData.observe(this, {
            if (it) {
                AssignStockViewModel.allowToGoNext.postValue(Pair(1, true))
                confirmAssignedItemsAdapter.notifyDataSetChanged()
            }
        })

        AssignStockViewModel.signedLiveData.observe(this) {
            if (it != null) {
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
                    AssignStockViewModel.assignedItemDetails.signatureInfoStoreKeeperFile =
                        it.second.toFile(requireContext())
                    ASSIGNMENT_RECEIVE_MODELS.salesSign.postValue(it.second)

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
                    AssignStockViewModel.assignedItemDetails.signatureInfoSalesPersonFile =
                        it.second.toFile(requireContext())

                }

                if (
                    AssignStockViewModel.assignedItemDetails.signatureInfoSalesPerson != null &&
                    AssignStockViewModel.assignedItemDetails.signatureInfoStoreKeeper != null
                ) {
                    AssignStockViewModel.allowToGoNext.postValue(Pair(2, true))
                }
            }
        }

    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        stock_assigned_confirm_loader.visible(visible)
    }

    override fun onResume() {
        super.onResume()
        confirm_assign_store_keeper_name.text =
            AssignStockViewModel.assignedItemDetails.storekeeperName


        if(AssignedItemDetails.isWarehouseAssignment){
            stock_assign_confirm_other_storekeeper_name.visibility = View.VISIBLE
            confirm_assign_salesperson_name.visibility = View.GONE
            confirm_assign_to_label.text = requireContext().getString(R.string.store_keeper)
            confirm_assign_sign_store_keeper.visibility = View.GONE
            confirm_assign_sign_salesperson.visibility = View.GONE
        }
        else{
            stock_assign_confirm_other_storekeeper_name.visibility = View.GONE
            confirm_assign_salesperson_name.visibility = View.VISIBLE
            confirm_assign_to_label.text = requireContext().getString(R.string.salesperson)
            confirm_assign_salesperson_name.text =
                AssignStockViewModel.assignedItemDetails.salespersonName
        }

        if (stock_assign_confirm_other_storekeeper_name.text.isNullOrEmpty()){
            AssignStockViewModel.allowToGoNext.postValue(Pair(2, false))
        }else{
            AssignedItemDetails.assignedToWarehouseStoreKeeperName = stock_assign_confirm_other_storekeeper_name.text.toString()
            AssignStockViewModel.allowToGoNext.postValue(Pair(2, true))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(storeKeeperTextWatcher.isNotNull()) {
            if (isAdded) {
                stock_assign_confirm_other_storekeeper_name.removeTextChangedListener(
                    storeKeeperTextWatcher
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ConfirmAssignedStockFragment()
    }

}