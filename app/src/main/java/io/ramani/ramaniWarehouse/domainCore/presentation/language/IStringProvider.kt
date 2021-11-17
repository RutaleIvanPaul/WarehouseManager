package io.ramani.ramaniWarehouse.domainCore.presentation.language

import androidx.annotation.StringRes

interface IStringProvider {
    fun getString(@StringRes stringResId: Int): String

    fun getString(@StringRes stringResId: Int, vararg formatArgs: String): String


    fun getConnectionErrorMessage(): String
}