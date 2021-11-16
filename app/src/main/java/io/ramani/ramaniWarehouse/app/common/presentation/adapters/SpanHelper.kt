package io.ramani.ramaniWarehouse.app.common.presentation.adapters

import android.content.Context
import android.content.res.Configuration
import io.ramani.ramaniWarehouse.app.common.presentation.extensions.isTablet
import org.jetbrains.anko.configuration

object SpanHelper {
    private const val PHONE_PORTRAIT_SPAN_COUNT = 2
    private const val TABLET_PORTRAIT_SPAN_COUNT = 3
    private const val TABLET_LANDSCAPE_SPAN_COUNT = 4

    fun getGridSpanCount(context: Context?): Int {
        val config = context?.configuration
        var spanCount = PHONE_PORTRAIT_SPAN_COUNT
        val isTablet = context?.isTablet() ?: false
        if (isTablet) {
            when (config?.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> spanCount = TABLET_PORTRAIT_SPAN_COUNT
                Configuration.ORIENTATION_LANDSCAPE -> spanCount = TABLET_LANDSCAPE_SPAN_COUNT
            }
        }
        return spanCount
    }
}