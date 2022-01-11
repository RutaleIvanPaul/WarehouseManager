package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveStock

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow.ReceiveStockFlow
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow.ReceiveStockFlowController
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.ProductModelView
import kotlinx.android.synthetic.main.fragment_confirm_stock_receive.*
import org.kodein.di.generic.factory


class ConfirmReceiveStockFragment : BaseFragment() {
    companion object {
        fun newInstance() = ConfirmReceiveStockFragment()
    }

    private val viewModelProvider: (Fragment) -> ConfirmReceiveViewModel by factory()
    private lateinit var viewModel: ConfirmReceiveViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel
    private lateinit var flow: ReceiveStockFlow

    override fun getLayoutResId(): Int = R.layout.fragment_confirm_stock_receive

    private lateinit var productsAdapter: ConfirmReceiveProductAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        subscribeObservers()
        viewModel.start()
    }

    private fun subscribeObservers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        observeLoadingVisible(viewModel, this)
        subscribeError(viewModel)
        observerError(viewModel, this)
        observeOnRefreshReceivingProductList()
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = ReceiveStockFlowController(baseActivity!!)
        setupRV()
    }

    private fun setupRV() {
        val products = mutableListOf<ProductModelView>()
        products.add(
            ProductModelView.Builder()
                .viewType(ProductModelView.TYPE.LABEL)
                .productName(getString(R.string.qty).capitalize())
                .temp(getString(R.string.status).capitalize())
                .isReceived(false)
                .build()
        )
        products.addAll(
            RECEIVE_MODELS.invoiceModelView?.products?.toMutableList() ?: mutableListOf()
        )
        productsAdapter = ConfirmReceiveProductAdapter(products) {
            flow.openConfirmProductSheet(it.productId ?: "") {
                productsAdapter.notifyDataSetChanged()
                RECEIVE_MODELS.refreshReceiveProductListLiveData.postValue(true)
            }
        }
        products_rv.apply {
            this.adapter = productsAdapter
        }
    }

    private fun observeOnRefreshReceivingProductList() {
        RECEIVE_MODELS.refreshReceiveProductListLiveData.observe(this, {
            productsAdapter.notifyDataSetChanged()
        })
    }
}