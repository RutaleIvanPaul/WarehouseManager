package io.ramani.ramaniWarehouse.app.common.presentation.extensions

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.jetbrains.anko.AnkoException
import java.io.Serializable

/**
 * Created by Amr on 9/7/17.
 */
fun Fragment.setArgs(vararg params: Pair<String, Any?>) {
    val bundle = Bundle()

    params.forEach {
        val value = it.second
        when (value) {
            null -> bundle.putSerializable(it.first, null as Serializable?)
            is Int -> bundle.putInt(it.first, value)
            is Long -> bundle.putLong(it.first, value)
            is CharSequence -> bundle.putCharSequence(it.first, value)
            is String -> bundle.putString(it.first, value)
            is Float -> bundle.putFloat(it.first, value)
            is Double -> bundle.putDouble(it.first, value)
            is Char -> bundle.putChar(it.first, value)
            is Short -> bundle.putShort(it.first, value)
            is Boolean -> bundle.putBoolean(it.first, value)
            is Serializable -> bundle.putSerializable(it.first, value)
            is Bundle -> bundle.putBundle(it.first, value)
            is Parcelable -> bundle.putParcelable(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> bundle.putSerializable(it.first, value)
                value.isArrayOf<String>() -> bundle.putSerializable(it.first, value)
                value.isArrayOf<Parcelable>() -> bundle.putSerializable(it.first, value)
                else -> throw AnkoException("bundle extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> bundle.putIntArray(it.first, value)
            is LongArray -> bundle.putLongArray(it.first, value)
            is FloatArray -> bundle.putFloatArray(it.first, value)
            is DoubleArray -> bundle.putDoubleArray(it.first, value)
            is CharArray -> bundle.putCharArray(it.first, value)
            is ShortArray -> bundle.putShortArray(it.first, value)
            is BooleanArray -> bundle.putBooleanArray(it.first, value)
            else -> throw AnkoException("bundle extra ${it.first} has wrong type ${value.javaClass.name}")
        }
    }

    arguments = bundle
}

fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view?.rootView?.windowToken, 0)

    view?.let { activity?.hideKeyboard(it) }

}

fun Fragment.startDialAction(phoneNumber: String) {
    context?.startDialAction(phoneNumber)
}

fun Fragment.createView(@LayoutRes layoutRes: Int, parent: ViewGroup? = null, attachToParent: Boolean = false,
                        init: View.() -> Unit = {}) =
        context?.createView(layoutRes, parent, attachToParent, init)