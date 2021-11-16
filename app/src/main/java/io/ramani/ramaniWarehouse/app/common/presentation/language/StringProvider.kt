package io.ramani.ramaniWarehouse.app.common.presentation.language

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.core.domain.presentation.language.IStringProvider

@SuppressLint("StaticFieldLeak")
object StringProvider : IStringProvider {

//    override fun getInspirationsString(): String =
//            getString(R.string.inspirations_library)
//
//    override fun getGenderList(): List<String> =
//            context?.resources?.getStringArray(R.array.gender_list)?.toList() ?: emptyList()
//
//    override fun getBothChildrenAndAdultsString(): String =
//            getString(R.string.both_children_adults_input)
//
//    override fun getChildrenOnlyString(): String =
//            getString(R.string.children_only_input)
//
//    override fun getAdultsOnlyString(): String =
//            getString(R.string.adults_only_input)
//
//    override fun getChildDevelopmentString(): String =
//            getString(R.string.child_development)
//
//    override fun getHolidayFeedBackString(): String =
//            getString(R.string.holiday_feedback)
//
//    override fun getDocumentsString(): String =
//            getString(R.string.documents)

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