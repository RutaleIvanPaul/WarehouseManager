package io.ramani.ramaniWarehouse.app.common.presentation.language

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.domainCore.presentation.language.IStringProvider

@SuppressLint("StaticFieldLeak")
object StringProvider : IStringProvider {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    override fun getString(@StringRes stringResId: Int) =
        context?.getString(stringResId) ?: ""

    override fun getString(@StringRes stringResId: Int, vararg formatArgs: String) =
        context?.getString(stringResId, formatArgs) ?: ""

    override fun getConnectionErrorMessage(): String =
        getString(R.string.oops_no_signal_check_your_internet_connection)
}