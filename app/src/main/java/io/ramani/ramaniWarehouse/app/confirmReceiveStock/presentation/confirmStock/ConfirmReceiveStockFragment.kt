package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.confirmStock

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.stockreceive.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.app.stockreceive.model.STOCK_RECEIVE_MODEL
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSignaturePadSheetFragment
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import kotlinx.android.synthetic.main.fragment_confirm_receive_stock.*
import org.kodein.di.generic.factory

class ConfirmReceiveStockFragment : BaseFragment() {
    companion object {
        fun newInstance() = ConfirmReceiveStockFragment()
    }

    private val viewModelProvider: (Fragment) -> ConfirmReceiveViewModel by factory()
    private lateinit var viewModel: ConfirmReceiveViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel
    private lateinit var flow: StockReceiveFlow
    private lateinit var productsAdapter: ConfirmedProductAdapter
    override fun getLayoutResId(): Int = R.layout.fragment_confirm_receive_stock
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        STOCK_RECEIVE_MODEL.clearData()
        subscribeObservers()
        viewModel.start()
    }

    private fun subscribeObservers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel, this)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeWhenReceiveSign()
    }

    private fun subscribeWhenReceiveSign() {
        STOCK_RECEIVE_MODEL.signedLiveData.observe(this, {

            if (it.first == StockReceiveSignaturePadSheetFragment.PARAM_STORE_KEEPER_SIGN) {
                stock_receive_confirm_store_keeper_name.isEnabled = false

                stock_receive_confirm_sign_store_keeper.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check_icon
                    ), null, null, null
                )
                stock_receive_confirm_sign_store_keeper.setBackgroundResource(R.drawable.green_stroke_action_button)
                stock_receive_confirm_sign_store_keeper.setText(R.string.signed)
                stock_receive_confirm_sign_store_keeper.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_lime_yellow
                    )
                )

                RECEIVE_MODELS.invoiceModelView?.storeKeeperName = viewModel.storeKeeperName
                RECEIVE_MODELS.invoiceModelView?.storeKeeperSign = it.second

            } else if (it.first == StockReceiveSignaturePadSheetFragment.PARAM_DELIVERY_PERSON_SIGN) {

                stock_receive_confirm_delivery_person_name.isEnabled = false

                stock_receive_confirm_sign_delivery_person.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check_icon
                    ), null, null, null
                )
                stock_receive_confirm_sign_delivery_person.setBackgroundResource(R.drawable.green_stroke_action_button)
                stock_receive_confirm_sign_delivery_person.setText(R.string.signed)
                stock_receive_confirm_sign_delivery_person.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_lime_yellow
                    )
                )

                val deliveryPersonName = stock_receive_confirm_delivery_person_name.text.toString()
                RECEIVE_MODELS.invoiceModelView?.deliveryPersonName = deliveryPersonName
                RECEIVE_MODELS.invoiceModelView?.deliveryPersonSign = it.second
            }
        })
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)
        setupRV()
        setupSignpad()
    }

    private fun setupSignpad() {
        stock_receive_confirm_store_keeper_name.text = viewModel.storeKeeperName
        // Stock keeper sign
        stock_receive_confirm_sign_store_keeper.setOnSingleClickListener {
            flow.openSignaturePad(StockReceiveSignaturePadSheetFragment.PARAM_STORE_KEEPER_SIGN)
        }

        // Delivery Person sign
        stock_receive_confirm_sign_delivery_person.setOnSingleClickListener {
            val signedName = stock_receive_confirm_delivery_person_name.text.toString()

            if (signedName.isNotEmpty()) {
                flow.openSignaturePad(StockReceiveSignaturePadSheetFragment.PARAM_DELIVERY_PERSON_SIGN)
            } else {
                errorDialog("Please enter the delivery person name")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupRV()
    }


    private fun setupRV() {
        val products = mutableListOf<ProductModelView>()
        products.add(
            ProductModelView.Builder()
                .viewType(ProductModelView.TYPE.LABEL)
                .productName(getString(R.string.accepted).capitalize())
                .temp(getString(R.string.declined).capitalize())
                .isReceived(false)
                .build()
        )

        RECEIVE_MODELS.invoiceModelView?.products?.forEach {
            if (it.isReceived == true) {
                products.add(it)
            }
        }

        productsAdapter = ConfirmedProductAdapter(products)
        products_rv.apply {
            this.adapter = productsAdapter
        }
    }


}