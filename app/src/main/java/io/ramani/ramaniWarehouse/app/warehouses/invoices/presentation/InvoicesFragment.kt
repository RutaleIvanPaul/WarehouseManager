package io.ramani.ramaniWarehouse.app.warehouses.invoices.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow.InvoicesFlow
import io.ramani.ramaniWarehouse.app.warehouses.mainNav.flow.InvoicesFlowController
import kotlinx.android.synthetic.main.fragment_invoice.*
import org.kodein.di.generic.factory

class InvoicesFragment : BaseFragment() {
    private lateinit var invoiceAdapter: InvoiceAdapter

    companion object {
        fun newInstance() = InvoicesFragment()
    }

    private val viewModelProvider: (Fragment) -> InvoicesViewModel by factory()
    private lateinit var viewModel: InvoicesViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel
    private lateinit var flow: InvoicesFlow

    override fun getLayoutResId(): Int = R.layout.fragment_invoice
    private var isLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        flow = InvoicesFlowController(baseActivity!!)
        viewModel.start()
        invoiceAdapter = InvoiceAdapter(viewModel.invoicesList) {
            flow.openConfirmReceiveStock(it.createdAt, it.supplierName, it.purchaseOrderId)
        }
        initSubscribers()
    }

    override fun initView(view: View?) {
        super.initView(view)
        invoices_rv.apply {
            adapter = invoiceAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val visibleItemCount = layoutManager?.childCount ?: 0
                    val totalItemCount = layoutManager?.itemCount ?: 0
                    val firstVisibleItemPosition =
                        (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()


                    if (!isLoading && viewModel.hasMoreToLoad) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                            viewModel.loadMore()
                            isLoading = true
                        }
                    }
                }

            })
        }
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        observeLoadingVisible(viewModel, this)
        subscribeOnInvoicesRetrieved()
    }

    private fun subscribeOnInvoicesRetrieved() {
        viewModel.onInvoicesLoadedLiveData.observe(this, {
            invoiceAdapter.notifyDataSetChanged()
            isLoading = false
            if (viewModel.invoicesList.isEmpty()) {
                no_data_tv.visible()
            } else {
                no_data_tv.visible(false)
            }
        })
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }


    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }
}