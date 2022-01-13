package io.ramani.ramaniWarehouse.app.returnstock.presentation.confirm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.williamww.silkysignature.views.SignaturePad
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.BaseBottomSheetDialogFragment
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.returnstock.presentation.host.ReturnStockViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveSignaturePadSheetFragment
import io.ramani.ramaniWarehouse.domainCore.printer.processForPrinting
import kotlinx.android.synthetic.main.fragment_return_stock_signature_pad.*
import kotlinx.android.synthetic.main.fragment_stock_receive_signature_sheet.*
import kotlinx.android.synthetic.main.fragment_stock_receive_signature_sheet.stock_receive_signature_pad_clear
import org.kodein.di.generic.factory

class ReturnStockSignaturePadFragment : BaseBottomSheetDialogFragment() {
    private val viewModelProvider: (Fragment) -> ReturnStockViewModel by factory()
    private lateinit var viewModel: ReturnStockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel
    private var isSigned = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return_stock_signature_pad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        return_stock_signature_pad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {}

            override fun onSigned() {
                isSigned = true
            }

            override fun onClear() {
                isSigned = false
            }
        })


        return_stock_signature_pad_clear.setOnSingleClickListener {
            stock_receive_signature_pad.clear()
        }

        return_stock_signature_pad_confirm.setOnSingleClickListener {
            if (!isSigned) {
                errorDialog("Please sign and click confirm.")
            } else {
                val bitmap = return_stock_signature_pad.signatureBitmap.processForPrinting()

                when (arguments?.getString("signee")) {
                    ReturnStockSignaturePadFragment.PARAM_STORE_KEEPER_SIGN -> ReturnStockViewModel.signedLiveData.postValue(Pair(
                        ReturnStockSignaturePadFragment.PARAM_STORE_KEEPER_SIGN, bitmap))
                    ReturnStockSignaturePadFragment.PARAM_SALESPERSON_SIGN -> ReturnStockViewModel.signedLiveData.postValue(Pair(
                        ReturnStockSignaturePadFragment.PARAM_SALESPERSON_SIGN, bitmap))
                }

                dismiss()
            }
        }
    }

    companion object {
        const val PARAM_STORE_KEEPER_SIGN = "Store_Keeper_Sign"
        const val PARAM_SALESPERSON_SIGN = "SalesPerson_Sign"
        @JvmStatic
        fun newInstance(signee: String): ReturnStockSignaturePadFragment {
            val fragment = ReturnStockSignaturePadFragment()

            val args = Bundle()
            args.putString("signee", signee)

            fragment.arguments = args
            return fragment
        }
    }
}