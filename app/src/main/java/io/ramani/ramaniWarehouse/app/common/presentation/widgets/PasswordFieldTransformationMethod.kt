package io.ramani.ramaniWarehouse.app.common.presentation.widgets

import android.text.method.PasswordTransformationMethod
import android.view.View


class PasswordFieldTransformationMethod(private val maskCharacter: Char) : PasswordTransformationMethod() {

    override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
        return PasswordCharSequence(source, maskCharacter)
    }

    class PasswordCharSequence(private var mSource: CharSequence?, private val maskCharacter: Char) : CharSequence {

        override val length: Int
            get() = mSource?.length ?: 0

        override fun get(index: Int): Char {
            return maskCharacter
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource?.subSequence(startIndex, endIndex) ?: ""
        }

    }
}