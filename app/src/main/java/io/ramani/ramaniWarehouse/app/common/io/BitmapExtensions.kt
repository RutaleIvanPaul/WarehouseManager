package io.ramani.ramaniWarehouse.app.common.io

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * Created by Raed Ezzat on 15/12/2017.
 */
fun Int.getBitmapFromResources(context: Context): Bitmap {
    var drawable = AppCompatResources.getDrawable(context, this)
        ?: AppCompatResources.getDrawable(context, R.drawable.common_full_open_on_phone)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        drawable = DrawableCompat.wrap(drawable!!).mutate()
    }

    val bitmap = Bitmap.createBitmap(
        drawable?.intrinsicWidth!!,
        drawable?.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    drawable.draw(canvas)

    return bitmap
}

fun Drawable.getBitmapFromDrawable(context: Context): Bitmap {
    var drawable = this
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        drawable = DrawableCompat.wrap(drawable!!).mutate()
    }

    val bitmap = Bitmap.createBitmap(
        drawable?.intrinsicWidth!!,
        drawable?.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    drawable.draw(canvas)

    return bitmap
}

fun getPixelsFromDPs(context: Context, dps: Int): Int {
    val r = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, r.getDimension(dps), r.displayMetrics
    ).toInt()
}


fun Bitmap.toFile(fileName: String = "${Calendar.getInstance().timeInMillis}.jpg"): File? =
    bitmapToFile(this, fileName)

fun Bitmap.toByteArray(fileName: String = "${Calendar.getInstance().timeInMillis}.jpg"): ByteArray? =
    bitmapToByteArray(this, fileName)

fun bitmapToByteArray(bitmap: Bitmap, fileName: String): ByteArray? {
    var bitmapdata: ByteArray? = null
    return try {
        val directory =
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath}/${BuildConfig.APP_NAME}/")
        if (!directory.exists())
            directory.mkdir()

        val file = File(directory, fileName)

        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos) // YOU can also save it in JPEG
        bitmapdata = bos.toByteArray()
        bitmapdata
    } catch (e: Exception) {
        e.printStackTrace()
        bitmapdata // it will return null
    }
}

private fun bitmapToFile(
    bitmap: Bitmap,
    fileNameToSave: String
): File? { // File name like "image.png"
    //create a file to write bitmap data
    var file: File? = null
    return try {
        val directory =
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath}/${BuildConfig.APP_NAME}/")
        if (!directory.exists())
            directory.mkdir()

        val file = File(directory, fileNameToSave)

        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos) // YOU can also save it in JPEG
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        file
    } catch (e: Exception) {
        e.printStackTrace()
        file // it will return null
    }
}
