package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.tabs

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SelectedProductModel
import org.kodein.di.generic.factory
import io.ramani.ramaniWarehouse.domainCore.lang.isNotNull
import kotlinx.android.synthetic.main.fragment_stock_receive_confirm.*
import androidx.recyclerview.widget.DividerItemDecoration
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL.Companion.DATA_DELIVERY_PERSON_DATA
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL.Companion.DATA_STORE_KEEPER_DATA
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSignaturePadSheetFragment.Companion.PARAM_DELIVERY_PERSON_SIGN
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSignaturePadSheetFragment.Companion.PARAM_STORE_KEEPER_SIGN
import io.ramani.ramaniWarehouse.domain.stockreceive.model.selected.SignatureInfo

class StockReceiveConfirmFragment : BaseFragment() {
    companion object {
        fun newInstance() = StockReceiveConfirmFragment()
    }

    private val viewModelProvider: (Fragment) -> StockReceiveNowViewModel by factory()
    private lateinit var viewModel: StockReceiveNowViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_confirm

    private var products: ArrayList<SelectedProductModel>? = null
    private var listAdapter: StockReceiveConfirmProductRVAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()

        subscribeObservers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)
    }

    override fun onResume() {
        super.onResume()

        updateView()
    }

    private fun subscribeObservers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel, this)
        subscribeError(viewModel)
        observerError(viewModel, this)

        STOCK_RECEIVE_MODEL.signedLiveData.observe(this, {

            if (it.first == PARAM_STORE_KEEPER_SIGN) {
                stock_receive_confirm_store_keeper_name.isEnabled = false

                stock_receive_confirm_sign_store_keeper.setCompoundDrawables(ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_icon), null, null, null)
                stock_receive_confirm_sign_store_keeper.setBackgroundResource(R.drawable.green_stroke_action_button)
                stock_receive_confirm_sign_store_keeper.setText(R.string.signed)
                stock_receive_confirm_sign_store_keeper.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_lime_yellow))

                val storeKeeperName = stock_receive_confirm_store_keeper_name.text.toString()
                STOCK_RECEIVE_MODEL.setData(DATA_STORE_KEEPER_DATA, SignatureInfo(storeKeeperName, it.second))

            } else if (it.first == PARAM_DELIVERY_PERSON_SIGN) {

                stock_receive_confirm_delivery_person_name.isEnabled = false

                stock_receive_confirm_sign_delivery_person.setCompoundDrawables(ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_icon), null, null, null)
                stock_receive_confirm_sign_delivery_person.setBackgroundResource(R.drawable.green_stroke_action_button)
                stock_receive_confirm_sign_delivery_person.setText(R.string.signed)
                stock_receive_confirm_sign_delivery_person.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_lime_yellow))

                val deliveryPersonName = stock_receive_confirm_delivery_person_name.text.toString()
                STOCK_RECEIVE_MODEL.setData(DATA_DELIVERY_PERSON_DATA, SignatureInfo(deliveryPersonName, it.second))

            }

            checkIfGoNext()
        })
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        stock_receive_confirm_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun checkIfGoNext() {
        STOCK_RECEIVE_MODEL.supplierData?.let {
            if (it.supplier.isNotNull() && it.storeKeeperData.isNotNull() && it.deliveryPersonData.isNotNull())
                STOCK_RECEIVE_MODEL.allowToGoNextLiveData.postValue(Pair(2, true))
        }
    }

    fun updateView() {
        // Initialize UI
        if (STOCK_RECEIVE_MODEL.supplierData.products != null) {
            STOCK_RECEIVE_MODEL.supplierData.products.let {
                products = it as ArrayList<SelectedProductModel>

                // Initialize List View
                stock_receive_confirm_product_list.apply {
                    layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

                    listAdapter = StockReceiveConfirmProductRVAdapter(products!!){
                        STOCK_RECEIVE_MODEL.updateProductRequestLiveData.postValue(it)
                    }

                    adapter = listAdapter
                    addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
                }
            }
        }

        // Stock keeper sign
        stock_receive_confirm_store_keeper_name.isEnabled = false
        stock_receive_confirm_store_keeper_name.setText(viewModel.userName)

        stock_receive_confirm_sign_store_keeper.setOnSingleClickListener {
            val signedName = stock_receive_confirm_store_keeper_name.text.toString()

            if (signedName.isNotEmpty()) {
                flow.openSignaturePad(PARAM_STORE_KEEPER_SIGN)
            } else {
                errorDialog("Please enter the stock keeper name")
            }
        }

        // Delivery Person sign
        stock_receive_confirm_sign_delivery_person.setOnSingleClickListener {
            val signedName = stock_receive_confirm_delivery_person_name.text.toString()

            if (signedName.isNotEmpty()) {
                flow.openSignaturePad(PARAM_DELIVERY_PERSON_SIGN)
            } else {
                errorDialog("Please enter the delivery person name")
            }
        }
    }
}