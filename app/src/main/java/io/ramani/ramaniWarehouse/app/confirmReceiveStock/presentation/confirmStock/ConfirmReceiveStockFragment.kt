package io.ramani.ramaniWarehouse.app.confirmReceiveStock.presentation.confirmStock

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.gone
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.visible
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
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.textColor
import org.kodein.di.generic.factory
import java.io.File
import java.util.*


class ConfirmReceiveStockFragment : BaseFragment() {
    private var capturedImage: File? = null
    private var capturePhotoActivityResult: ActivityResultLauncher<Intent>? = null

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
        setupOnActivityResult()
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
        STOCK_RECEIVE_MODEL.signedLiveData.observe(this) {

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
                store_keeper_captured_image_preview.visible()
                store_keeper_captured_image_preview.setImageBitmap(it.second)

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
                delivery_person_captured_image_preview.visible()
                delivery_person_captured_image_preview.setImageBitmap(it.second)
            }
        }
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)
        setupRV()
        setupDeliveryPersonSignature()
        setupSignpad()
    }

    private fun setupDeliveryPersonSignature() {

//        radio_group.setOnCheckedChangeListener { _, checkedId ->
//            when (checkedId) {
//                R.id.signature_radio_button -> {
//                    stock_receive_confirm_sign_delivery_person.text = getString(R.string.sign)
//                    resetButtonStyle()
//                    resetData()
//                }
//
//                R.id.doc_radio_button -> {
//                    stock_receive_confirm_sign_delivery_person.text = getString(R.string.capture)
//                    resetButtonStyle()
//                    resetData()
//                }
//            }
//        }
    }

    private fun resetData() {
        delivery_person_captured_image_preview.gone()
        RECEIVE_MODELS.invoiceModelView?.deliveryPersonSign = null
        capturedImage = null
    }

    private fun resetButtonStyle() {
        stock_receive_confirm_sign_delivery_person.textColor =
            ContextCompat.getColor(requireContext(), R.color.secondary_blue)
        val sdk = android.os.Build.VERSION.SDK_INT
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stock_receive_confirm_sign_delivery_person.backgroundDrawable =
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.blue_stroke_action_button
                )

        } else {
            stock_receive_confirm_sign_delivery_person.background =
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.blue_stroke_action_button
                )
        }
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

//            if (signedName.isNotEmpty()) {
//                if (signature_radio_button.isChecked) {
//                    flow.openSignaturePad(StockReceiveSignaturePadSheetFragment.PARAM_DELIVERY_PERSON_SIGN)
//                } else {
//                    if (ContextCompat.checkSelfPermission(
//                            requireContext(),
//                            Manifest.permission.CAMERA
//                        )
//                        == PackageManager.PERMISSION_DENIED
//                    ) {
//                        ActivityCompat.requestPermissions(
//                            requireActivity(),
//                            arrayOf(Manifest.permission.CAMERA),
//                            REQUEST_CAMERA
//                        )
//
//                    } else {
//                        openCameraActivity()
//                    }
//                }
//            } else {
//                errorDialog("Please enter the delivery person name")
//            }
        }
    }

    private fun openCameraActivity() {

        val directory =
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath}/${BuildConfig.APP_NAME}/")
        if (!directory.exists())
            directory.mkdir()

        val fileNameToSave = "${Calendar.getInstance().timeInMillis}.jpg"

        capturedImage = File(directory, fileNameToSave)

        capturedImage?.createNewFile()

//        val outputFileUri = Uri.fromFile(file)
        // Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", createImageFile());
        val outputFileUri = FileProvider.getUriForFile(
            requireContext(),
            "${context?.applicationContext?.packageName}.provider",
            capturedImage ?: File("")
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
        capturePhotoActivityResult?.launch(cameraIntent)
//        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE)
    }

    private fun setupOnActivityResult() {
        capturePhotoActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                stock_receive_confirm_sign_delivery_person.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check_icon
                    ), null, null, null
                )
                stock_receive_confirm_sign_delivery_person.setBackgroundResource(R.drawable.green_stroke_action_button)
                stock_receive_confirm_sign_delivery_person.setText(R.string.captured)
                stock_receive_confirm_sign_delivery_person.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_lime_yellow
                    )
                )
                val image = BitmapFactory.decodeFile(capturedImage?.absolutePath)
                val deliveryPersonName = stock_receive_confirm_delivery_person_name.text.toString()
                RECEIVE_MODELS.invoiceModelView?.deliveryPersonName = deliveryPersonName
                RECEIVE_MODELS.invoiceModelView?.deliveryPersonSign = image
                delivery_person_captured_image_preview.visible()
                delivery_person_captured_image_preview.setImageBitmap(image)
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