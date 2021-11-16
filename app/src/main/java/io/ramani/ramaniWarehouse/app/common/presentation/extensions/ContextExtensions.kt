package io.ramani.ramaniWarehouse.app.common.presentation.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.style.DrawableMarginSpan
import android.text.style.ImageSpan
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import io.ramani.ramaniWarehouse.R
import org.jetbrains.anko.layoutInflater

fun Context.isTablet() = resources.getBoolean(R.bool.isTablet)

fun Context.startDialAction(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }

    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

fun Context.imageSpan(@DrawableRes drawableRes: Int) =
        ImageSpan(this, drawableRes, ImageSpan.ALIGN_BOTTOM)

fun Context.drawableMarginSpan(@DrawableRes drawableRes: Int, @DimenRes marginRes: Int) =
        DrawableMarginSpan(drawable(drawableRes)!!, resources.getDimensionPixelSize(marginRes))

inline fun Context.createView(@LayoutRes layoutRes: Int,
                              code95: ViewGroup? = null,
                              attachTocode95: Boolean = false,
                              init: View.() -> Unit = {}): View =
        layoutInflater.inflate(layoutRes, code95, attachTocode95).apply {
            init(this)
        }