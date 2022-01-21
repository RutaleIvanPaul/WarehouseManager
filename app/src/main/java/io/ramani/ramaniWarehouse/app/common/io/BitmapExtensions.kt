package io.ramani.ramaniWarehouse.app.common.io

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import io.ramani.ramaniWarehouse.BuildConfig
import io.ramani.ramaniWarehouse.R
import java.io.*
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


fun Bitmap.toFile(
    context: Context,
    fileName: String = "${Calendar.getInstance().timeInMillis}.jpg"
): File? =
    bitmapToFile(this, fileName, context)

private fun bitmapToFile(
    bitmap: Bitmap,
    fileNameToSave: String,
    context: Context
): File? { // File name like "image.png"
    //create a file to write bitmap data
    var file: File? = null
    return try {
        if (Build.VERSION.SDK_INT == 29) {
            file = saveBitmapQ(context, fileNameToSave, bitmap)
        } else {
            val directory =
                File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath}/${BuildConfig.APP_NAME}/")
            if (!directory.exists())
                directory.mkdir()

            val file = File(directory, fileNameToSave)

            file.createNewFile()
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            //Convert bitmap to byte array
        }

        file
    } catch (e: Exception) {
        e.printStackTrace()
        file // it will return null
    }
}

private fun saveBitmapQ(
    context: Context, fileName: String, bitmap: Bitmap
): File? {
    //val relativeLocation = Environment.DIRECTORY_PICTURES
    var relativeLocation = Environment.DIRECTORY_DOWNLOADS + "/${BuildConfig.APP_NAME}/"
    //Insted of Environment.DIRECTORY_PICTURES this can be any valid derectory like Environment.DIRECTORY_DCIM, Environment.DIRECTORY_DOWNLOADS etc..
    // And insted of "KrishanImages" this can be any directory name you want to create for saving images
    val contentValues = ContentValues()
    contentValues.put(
        MediaStore.MediaColumns.DISPLAY_NAME,
        "$fileName"
    ) //this is the file name you want to save
    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg") // Content-Type
    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)

    val directory = File(relativeLocation)
    if (!directory.exists())
        directory.mkdir()

    val resolver = context.contentResolver
//    val resolver = appModule.createAnkoContext(app)

    var stream: OutputStream? = null
    var uri: Uri? = null

    try {
        uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        if (uri == null) {
            throw IOException("Failed to create new MediaStore record.")
        }
        stream = resolver.openOutputStream(uri!!)

        var bitmapStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapStream)
        val byteArray = bitmapStream.toByteArray()

        stream?.write(byteArray)
        bitmapStream?.close()
        stream?.close()

        return File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath}/${BuildConfig.APP_NAME}/$fileName")
//        return RequestBody.create(MediaType.parse("image/jpg"), file)

    } catch (e: IOException) {
        if (uri != null) {
            // Don't leave an orphan entry in the MediaStore
            resolver.delete(uri, null, null)
        }

        throw e
    } finally {
        if (stream != null) {
            stream!!.close()
        }
    }


//    fun saveBitmap(context: Context?, fileName: String,
//                            bitmap: Bitmap
//    ) : RequestBody {
//        var relativeLocation = Environment.DIRECTORY_PICTURES +"/${BuildConfig.APP_NAME}/"
//
//        val contentValues = ContentValues()
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName}" + ".jpg") //this is the file name you want to save
//        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg") // Content-Type
//        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
//
//        val resolver = context?.contentResolver
//
//        var stream: OutputStream? = null
//        var uri: Uri? = null
//
//        try {
//            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            uri = resolver?.insert(contentUri, contentValues)
//
//            if (uri == null) {
//                throw IOException("Failed to create new MediaStore record.")
//            }
//
//            stream = resolver?.openOutputStream(uri!!)
//
//            if (stream == null) {
//                throw IOException("Failed to get output stream.")
//            }
//
//            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream) == false) {
//                throw IOException("Failed to save bitmap.")
//            }
//            else{
//                return RequestBody.create(MediaType.parse("image/jpg"), bitmap.toFile()!!)
//            }
//        } catch (e: IOException) {
//            if (uri != null) {
//                // Don't leave an orphan entry in the MediaStore
//                resolver?.delete(uri!!, null, null)
//            }
//
//            throw e
//        } finally {
//            if (stream != null) {
//                stream!!.close()
//            }
//        }
//    }

}
