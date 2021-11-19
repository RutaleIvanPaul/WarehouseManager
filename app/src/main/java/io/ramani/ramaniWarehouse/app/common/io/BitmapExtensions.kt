package io.ramani.ramaniWarehouse.app.common.io

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import io.ramani.ramaniWarehouse.R


/**
 * Created by Raed Ezzat on 15/12/2017.
 */
fun Int.getBitmapFromResources(context: Context): Bitmap {
    var drawable = AppCompatResources.getDrawable(context, this)
            ?: AppCompatResources.getDrawable(context, R.drawable.common_full_open_on_phone)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        drawable = DrawableCompat.wrap(drawable!!).mutate()
    }

    val bitmap = Bitmap.createBitmap(drawable?.intrinsicWidth!!,
            drawable?.intrinsicHeight, Bitmap.Config.ARGB_8888)
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

    val bitmap = Bitmap.createBitmap(drawable?.intrinsicWidth!!,
            drawable?.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    drawable.draw(canvas)

    return bitmap
}

fun getPixelsFromDPs(context: Context, dps: Int): Int {
    val r = context.resources
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, r.getDimension(dps), r.displayMetrics).toInt()
}
