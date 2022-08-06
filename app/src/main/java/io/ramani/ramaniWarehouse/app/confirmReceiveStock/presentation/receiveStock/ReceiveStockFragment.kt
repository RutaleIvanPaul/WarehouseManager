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
import kotlinx.android.synthetic.main.fragment_receive_product.*
import org.kodein.di.generic.factory


class ReceiveStockFragment : BaseFragment() {
    companion object {
        fun newInstance() = ReceiveStockFragment()
    }

    private val viewModelProvider: (Fragment) -> ConfirmReceiveViewModel by factory()
    private lateinit var viewModel: ConfirmReceiveViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel
    private lateinit var flow: ReceiveStockFlow

    override fun getLayoutResId(): Int = R.layout.fragment_receive_product

    private lateinit var productsAdapter: ReceiveProductAdapter
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

    override fun onResume() {
        super.onResume()

        productsAdapter.setList(getProducts())
    }

    private fun setupRV() {
        val products = getProducts()
        productsAdapter = ReceiveProductAdapter(products) {
            flow.openConfirmProductSheet(it.productId ?: "") {
                productsAdapter.notifyDataSetChanged()
                RECEIVE_MODELS.refreshReceiveProductListLiveData.postValue(true)
            }
        }
        products_rv.apply {
            this.adapter = productsAdapter
        }
    }

    private fun getProducts(): MutableList<ProductModelView> {
        val products = mutableListOf<ProductModelView>()
        products.add(
            ProductModelView.Builder()
                .viewType(ProductModelView.TYPE.LABEL)
                .isReceived(false)
                .build()
        )
        products.addAll(
            RECEIVE_MODELS.invoiceModelView?.products?.toMutableList() ?: mutableListOf()
        )

        return products
    }

    private fun observeOnRefreshReceivingProductList() {
        RECEIVE_MODELS.refreshHostReceiveProductListLiveData.observe(this, {
            productsAdapter.notifyDataSetChanged()
        })
    }
}