package io.ramani.ramaniWarehouse.app.common.presentation.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File


/**
 * Created by Amr on 6/19/17.
 */
fun ImageView.loadCircleCroppedImage(
    imageUrl: String,
    @DrawableRes placeHolder: Int = -1,
    @DrawableRes errorPlaceHolder: Int = -1
) {
    loadImage(imageUrl, placeHolder, errorPlaceHolder, true)
}

fun ImageView.loadCircleCroppedImage(
    imageUri: Uri,
    @DrawableRes placeHolder: Int = -1,
    @DrawableRes errorPlaceHolder: Int = -1
) {
    loadImage(imageUri, placeHolder, errorPlaceHolder, true)
}

fun ImageView.loadRectangleRoundedImage(
    imageUri: Uri,
    @DrawableRes placeHolder: Int = -1,
    @DrawableRes errorPlaceHolder: Int = -1
) {
    var transformation = MultiTransformation(CenterCrop(), RoundedCorners(10))
    loadImage(imageUri, placeHolder, errorPlaceHolder, false, transformation)
}

fun ImageView.loadRectangleRoundedImage(
    imageUrl: String,
    @DrawableRes placeHolder: Int = -1,
    @DrawableRes errorPlaceHolder: Int = -1,
    cornerRadius: Int = 10
) {
    var transformation = MultiTransformation(CenterCrop(), RoundedCorners(cornerRadius))
    loadImage(imageUrl, placeHolder, errorPlaceHolder, false, transformation)
}

@SuppressLint("CheckResult")
fun ImageView.loadImage(
    imageUrl: String,
    @DrawableRes placeHolder: Int? = null,
    @DrawableRes errorPlaceHolder: Int? = null,
    circleCrop: Boolean = false,
    transformation: Transformation<Bitmap>? = null
) {
    Glide.with(this).asBitmap().load(imageUrl)
        .apply(RequestOptions().apply {
            if (placeHolder != null) {
                placeholder(placeHolder)
            }
            if (errorPlaceHolder != null) {
                error(errorPlaceHolder)
            }
            if (circleCrop) {
                circleCrop()
            }

            if (transformation != null) {
                transform(transformation)
            }
        })
        .into(this)
}

@SuppressLint("CheckResult")
fun View.loadImageIntoDrawable(
    imageUrl: String,
    context: Context,
    onSuccess: (Drawable) -> Unit = {},
    circleCrop: Boolean = false,
    transformation: Transformation<Bitmap>? = null
) {

    val singleTarget: SimpleTarget<Drawable> = object : SimpleTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            onSuccess(resource)
        }
    }
    Glide.with(context).load(imageUrl)
        .apply(RequestOptions().apply {
            if (circleCrop) {
                circleCrop()
            }
            if (transformation != null) {
                transform(transformation)
            }
        }).into(singleTarget)
}

fun View.loadImageIntoBitmap(
    imageUrl: String,
    @DrawableRes placeHolder: Int? = null,
    @DrawableRes errorPlaceHolder: Int? = null,
    circleCrop: Boolean = false,
    transformation: Transformation<Bitmap>? = null,
    onSuccess: (Bitmap) -> Unit
) {
    Glide.with(this)
        .asBitmap()
        .load(imageUrl)
        .apply(RequestOptions().apply {
            if (placeHolder != null) {
                placeholder(placeHolder)
            }
            if (errorPlaceHolder != null) {
                error(errorPlaceHolder)
            }
            if (circleCrop) {
                circleCrop()
            }
            if (transformation != null) {
                transform(transformation)
            }
        }).into(object : ViewTarget<View, Bitmap>(this) {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                resource.apply(onSuccess)
            }
        })
}

@SuppressLint("CheckResult")
fun ImageView.loadImage(
    imageUri: Uri,
    @DrawableRes placeHolder: Int? = null,
    @DrawableRes errorPlaceHolder: Int? = null,
    circleCrop: Boolean = false,
    transformation: Transformation<Bitmap>? = null
) {
    Glide.with(this).load(imageUri)
        .apply(RequestOptions().apply {
            if (placeHolder != null) {
                placeholder(placeHolder)
            }
            if (errorPlaceHolder != null) {
                error(errorPlaceHolder)
            }
            if (circleCrop) {
                circleCrop()
            }
            if (transformation != null) {
                transform(transformation)
            }
        }).into(this)
}

@SuppressLint("CheckResult")
fun ImageView.loadImage(
    file: File,
    @DrawableRes placeHolder: Int? = null,
    @DrawableRes errorPlaceHolder: Int? = null,
    circleCrop: Boolean = false,
    transformation: Transformation<Bitmap>? = null
) {
    Glide.with(this).load(file)
        .apply(RequestOptions().apply {
            if (placeHolder != null) {
                placeholder(placeHolder)
            }
            if (errorPlaceHolder != null) {
                error(errorPlaceHolder)
            }
            if (circleCrop) {
                circleCrop()
            }
            if (transformation != null) {
                transform(transformation)
            }
        }).into(this)
}

