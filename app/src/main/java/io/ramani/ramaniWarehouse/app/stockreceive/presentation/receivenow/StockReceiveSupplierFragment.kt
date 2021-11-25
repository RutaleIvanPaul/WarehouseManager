package io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.code95.android.app.auth.flow.StockReceiveFlowController
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.auth.flow.StockReceiveFlow
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.loadImage
import io.ramani.ramaniWarehouse.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniWarehouse.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniWarehouse.app.stockreceive.presentation.receivenow.StockReceiveNowViewModel.Companion.DATA_SUPPLIER
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import kotlinx.android.synthetic.main.fragment_stock_receive_supplier.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class StockReceiveSupplierFragment : BaseFragment() {
    companion object {
        fun newInstance() = StockReceiveSupplierFragment()
        private val IMAGE_DIRECTORY = "/RWTemp"
    }

    private val viewModelProvider: (Fragment) -> StockReceiveNowViewModel by factory()
    private lateinit var viewModel: StockReceiveNowViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: StockReceiveFlow

    private val dateFormatter: DateFormatter by instance()
    private val images:ArrayList<String> = ArrayList();
    private var gridViewAdapter: GridViewAdapter? = null

    override fun getLayoutResId(): Int = R.layout.fragment_stock_receive_supplier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)

    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = StockReceiveFlowController(baseActivity!!, R.id.main_fragment_container)

        // Initialize UI
        supplier_receiving_date_label.text = dateFormatter.getCalendarTimeString(Date())

        supplier_receiving_select_supplier_spinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            supplier_receiving_select_supplier_spinner.isSelected = true

            viewModel.getSuppliersActionLiveData.value?.get(newIndex)?.let {
                viewModel.setData(DATA_SUPPLIER, it)
            }
        }

        stock_receive_take_photo.setOnClickListener {
            showTakePhotoDialog()
        }

        gridViewAdapter = GridViewAdapter(requireActivity(), images)
        stock_receive_supplier_gallery.adapter = gridViewAdapter

        viewModel.start()
        subscribeObservers()
    }

    override fun onPause() {
        supplier_receiving_select_supplier_spinner.dismiss()

        super.onPause()
    }

    override fun onDestroy() {
        supplier_receiving_select_supplier_spinner.dismiss()

        super.onDestroy()
    }

    private fun subscribeObservers() {
        viewModel.validationResponseLiveData.observe(this, {

        })

        viewModel.getSuppliersActionLiveData.observe(this, {
            val items = ArrayList<String>()
            for (supplier in it)
                items.add(supplier.name)

            supplier_receiving_select_supplier_spinner.setItems(items)

        })
    }

    private fun updateGallery() {
        gridViewAdapter?.apply {
            setImages(images)
            notifyDataSetInvalidated()
        }

    }

    private fun showTakePhotoDialog() {
        val pictureDialog = AlertDialog.Builder(requireActivity())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select image from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> chooseImageFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncherForGallery.launch(intent)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncherForCamera.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private var resultLauncherForGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null)
            {
                val pictureUri = data.data
                pictureUri?.let {
                    val bitmap = getCapturedImage(it)
                    val path = saveImage(bitmap)
                    images.add(path)
                    updateGallery()
                }
            }
        }
    }

    private var resultLauncherForCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            val thumbnail = data!!.extras!!.get("data") as Bitmap
            val path = saveImage(thumbnail)
            images.add(path)
            updateGallery()
        }
    }

    private fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

        val wallpaperDirectory = File (requireActivity().getExternalFilesDir(null).toString() + IMAGE_DIRECTORY)
        if (!wallpaperDirectory.exists())
            wallpaperDirectory.mkdirs()

        try {
            val f = File(wallpaperDirectory, ((Calendar.getInstance().getTimeInMillis()).toString() + ".png"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(requireActivity(), arrayOf(f.getPath()), arrayOf("image/png"), null)
            fo.close()

            return f.getAbsolutePath()
        }
        catch (e1: IOException){
            e1.printStackTrace()
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap {
        val bitmap = when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                selectedPhotoUri
            )
            else -> {
                val source = ImageDecoder.createSource(requireActivity().contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }

        return bitmap
    }

    class GridViewAdapter(private val context: Context, var listImageURLs: List<String>) : BaseAdapter() {

        fun setImages(images: List<String>) {
            listImageURLs = images
        }

        override fun getCount(): Int {
            return listImageURLs.size
        }

        override fun getItem(position: Int): Any {
            return listImageURLs[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val viewHolder: ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_gallery, null)
                viewHolder = ViewHolder()
                viewHolder.imageView = convertView!!.findViewById(R.id.item_gallery)
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            viewHolder.imageView?.apply {
                layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, 150)
                loadImage(listImageURLs[position])
            }

            return convertView
        }

        internal inner class ViewHolder {
            var imageView: ImageView? = null
        }
    }

}