package io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlow
import io.ramani.ramaniWarehouse.app.returnstock.flow.ReturnStockFlowcontroller
import io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm.model.ReturnItemDetails
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import kotlinx.android.synthetic.main.fragment_confirm_return_stock.*
import org.kodein.di.generic.factory


class ConfirmReturnStockFragment : BaseFragment() {
    private lateinit var confirmReturnItemsAdapter: ConfirmReturnItemsAdapter
    private val viewModelProvider: (Fragment) -> ConfirmReturnStockViewModel by factory()
    private lateinit var viewModel: ConfirmReturnStockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: ReturnStockFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)

        confirmReturnItemsAdapter =
            ConfirmReturnItemsAdapter(ReturnItemDetails.returnItems) {}
    }


    override fun getLayoutResId() = R.layout.fragment_confirm_return_stock

    override fun initView(view: View?) {
        super.initView(view)
        confirm_return_items_RV.layoutManager = LinearLayoutManager(requireContext())
        confirm_return_items_RV.adapter = confirmReturnItemsAdapter
        subscribeObservers()

        flow = ReturnStockFlowcontroller(baseActivity!!, R.id.main_fragment_container)

        confirm_return_sign_store_keeper.setOnSingleClickListener {
            flow.openReturnStockSignPad(ReturnStockSignaturePadFragment.PARAM_STORE_KEEPER_SIGN)
        }

        confirm_return_sign_salesperson.setOnSingleClickListener {
            flow.openReturnStockSignPad(ReturnStockSignaturePadFragment.PARAM_SALESPERSON_SIGN)
        }
    }

    private fun subscribeObservers() {
        viewModel.onItemsReturnedLiveData.observe(this, {
//            onItemsReturned(it)
        })

        ReturnStockViewModel.returnItemsChangedLiveData.observe(this, {
            if (it){
                ReturnStockViewModel.allowToGoNext.postValue(Pair(1, true))
                confirmReturnItemsAdapter.notifyDataSetChanged()
            }
        })

        ReturnStockViewModel.signedLiveData.observe(this, {
            if (it.first == ReturnStockSignaturePadFragment.PARAM_STORE_KEEPER_SIGN) {

                confirm_return_sign_store_keeper.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check_icon
                    ), null, null, null
                )
                confirm_return_sign_store_keeper.setBackgroundResource(R.drawable.green_stroke_action_button)
                confirm_return_sign_store_keeper.setText(R.string.signed)
                confirm_return_sign_store_keeper.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_lime_yellow
                    )
                )

                ReturnItemDetails.signatureInfoStoreKeeper = it.second

            } else if (it.first == ReturnStockSignaturePadFragment.PARAM_SALESPERSON_SIGN) {

                confirm_return_sign_salesperson.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check_icon
                    ), null, null, null
                )
                confirm_return_sign_salesperson.setBackgroundResource(R.drawable.green_stroke_action_button)
                confirm_return_sign_salesperson.setText(R.string.signed)
                confirm_return_sign_salesperson.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_lime_yellow
                    )
                )

                ReturnItemDetails.signatureInfoSalesPerson = it.second

            }

            if(
                ReturnItemDetails.signatureInfoSalesPerson != null &&
                ReturnItemDetails.signatureInfoStoreKeeper != null
            ){
                ReturnStockViewModel.allowToGoNext.postValue(Pair(2,true))
            }
        })


    }

    override fun onResume() {
        super.onResume()
        confirm_return_store_keeper_name.text =
            ReturnItemDetails.storekeeperName
        confirm_return_salesperson_name.text =
            ReturnItemDetails.salespersonName
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ConfirmReturnStockFragment()
    }

}