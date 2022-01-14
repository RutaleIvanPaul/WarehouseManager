package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.host

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.adapters.TabPagerAdapter
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setArgs
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow.ReceiveStockFlow
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.flow.ReceiveStockFlowController
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.model.RECEIVE_MODELS
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.ConfirmReceiveViewModel
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.confirmStock.ConfirmReceiveStockFragment
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.receiveStock.ReceiveStockFragment
import io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.supplier.SupplierConfirmReceiveFragment
import io.ramani.ramaniWarehouse.app.warehouses.invoices.model.InvoiceModelView
import kotlinx.android.synthetic.main.fragment_signin_sheet.loader
import kotlinx.android.synthetic.main.fragment_stock_receive_now_host.*
import org.kodein.di.generic.factory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


private const val INVOICE_MODEL_VIEW_ARG = "invoice_model_view_arg"

class ConfirmReceiveStockHostFragment : BaseFragment() {

    companion object {
        fun newInstance(invoiceModelView: InvoiceModelView?) =
            ConfirmReceiveStockHostFragment().apply {
                setArgs(
                    INVOICE_MODEL_VIEW_ARG to invoiceModelView
                )
            }
    }

    private val viewModelProvider: (Fragment) -> ConfirmReceiveViewModel by factory()
    private lateinit var viewModel: ConfirmReceiveViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: ReceiveStockFlow
    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_now_host

    //    private var invoiceModelView: InvoiceModelView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        flow = ReceiveStockFlowController(baseActivity!!)
        initArgs()
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        observeRefreshedProductList()
        observeOnGoodsPosted()
        viewModel.start()
    }

    private fun observeOnGoodsPosted() {
        viewModel.postGoodsReceivedActionLiveData.observe(this, {
            DrawableCompat.setTint(
                stock_receive_now_host_indicator_2.drawable,
                ContextCompat.getColor(requireContext(), R.color.ramani_green)
            )
            flow.openReceiveSuccess()
        })
    }

    private fun observeRefreshedProductList() {
        RECEIVE_MODELS.refreshReceiveProductListLiveData.observe(this, {
            if (RECEIVE_MODELS.invoiceModelView?.products?.all { it.isReceived == true } == true) {
                turnMarkOneToGreen()
            }
        })
    }

    private fun initArgs() {
        RECEIVE_MODELS.invoiceModelView = arguments?.getParcelable(INVOICE_MODEL_VIEW_ARG)
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun initView(view: View?) {
        super.initView(view)
        stock_receive_now_host_back.setOnClickListener {
            onBackButtonPressed()
        }
        flow = ReceiveStockFlowController(baseActivity!!)
        initTabLayout()
        stock_receive_now_host_next_button.setOnClickListener {
            when (stock_receive_now_host_viewpager.currentItem) {
                0 -> {
                    stock_receive_now_host_viewpager.currentItem++
                    stock_receive_now_host_indicator_1.visible()
                }
                1 -> {
                    if (RECEIVE_MODELS.invoiceModelView?.products?.all { it.isReceived == true } == true) {
                        turnMarkOneToGreen()
                        stock_receive_now_host_indicator_2.visible()
                        stock_receive_now_host_viewpager.currentItem++
                        stock_receive_now_host_next_button.text =
                            getString(R.string.done).capitalize()
                    } else {
                        flow.openConfirmProductSheet(
                            RECEIVE_MODELS.invoiceModelView?.products?.first { it.isReceived == false }?.productId
                                ?: ""
                        ) {
                            if (RECEIVE_MODELS.invoiceModelView?.products?.all { it.isReceived == true } == true) {
                                turnMarkOneToGreen()
                            }
                            RECEIVE_MODELS.refreshHostReceiveProductListLiveData.postValue(true)
                        }
                    }
                }
                else -> {
                    viewModel.postGoodsReceived(
                        RECEIVE_MODELS.invoiceModelView?.storeKeeperSign,
                        RECEIVE_MODELS.invoiceModelView?.deliveryPersonSign
                    )
                }
            }
//            if (stock_receive_now_host_viewpager.currentItem < 2) {
//                stock_receive_now_host_viewpager.currentItem++
//            }
        }
    }

    private fun createFileFromBitmap(signature: Bitmap?): File? {
        //create a file to write bitmap data
        if (signature != null) {
            var file: File? = null
            return try {
                val directory = File(
                    "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath}/${BuildConfig.APP_NAME}/"
                )

                if (!directory.exists())
                    directory.mkdir()

                val file = File(directory, "signature_${Calendar.getInstance().timeInMillis}.png")
                file.createNewFile()

                //Convert bitmap to byte array
                val bos = ByteArrayOutputStream()
                signature?.compress(
                    Bitmap.CompressFormat.PNG,
                    0,
                    bos
                ) // YOU can also save it in JPEG
                val bitmapdata = bos.toByteArray()

                //write the bytes in file
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                return file
            } catch (e: Exception) {
                e.printStackTrace()
                return file // it will return null
            }
        }
        return null
    }

    private fun initTabLayout() {
        DrawableCompat.setTint(
            stock_receive_now_host_indicator_0.drawable,
            ContextCompat.getColor(requireContext(), R.color.ramani_green)
        )
        val adapter = TabPagerAdapter(activity)

        adapter.addFragment(
            SupplierConfirmReceiveFragment.newInstance(
                RECEIVE_MODELS.invoiceModelView?.createdAt,
                RECEIVE_MODELS.invoiceModelView?.supplierName,
                RECEIVE_MODELS.invoiceModelView?.purchaseOrderId
            ), getString(R.string.supplier)
        )
        adapter.addFragment(
            ReceiveStockFragment.newInstance(), getString(R.string.products)
        )

        adapter.addFragment(
            ConfirmReceiveStockFragment.newInstance(), getString(R.string.confirm)
        )

        stock_receive_now_host_viewpager.isUserInputEnabled = false
        stock_receive_now_host_viewpager.adapter = adapter
        stock_receive_now_host_viewpager.currentItem = 0
        TabLayoutMediator(
            stock_receive_now_host_tablayout,
            stock_receive_now_host_viewpager
        ) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
        stock_receive_now_host_tablayout.touchables.map { it.isClickable = false }
        stock_receive_now_host_viewpager.offscreenPageLimit = 2
    }

    private fun turnMarkOneToGreen() {
        DrawableCompat.setTint(
            stock_receive_now_host_indicator_1.drawable,
            ContextCompat.getColor(requireContext(), R.color.ramani_green)
        )
    }

    override fun onBackButtonPressed(): Boolean {
        if (stock_receive_now_host_viewpager.currentItem > 0) {
            stock_receive_now_host_next_button.text = getString(R.string.continue_)
            stock_receive_now_host_viewpager.currentItem--
            return true
        } else {
            return super.onBackButtonPressed()
        }
    }

}