package io.ramani.ramaniWarehouse.app.common.presentation.widgets

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager


class AutofitGridLayoutManager(context: Context,coulmnWidth: Int) : androidx.recyclerview.widget.GridLayoutManager(context, 1) {
    private var mColumnWidth: Int = 0
    private var mColumnWidthChanged = true
    private var mWidthChanged = true
    private var mWidth = 0




    fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != mColumnWidth) {
            mColumnWidth = newColumnWidth
            mColumnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: androidx.recyclerview.widget.RecyclerView.Recycler?, state: androidx.recyclerview.widget.RecyclerView.State) {
        val width = width
        val height = height

        if (width != mWidth) {
            mWidthChanged = true
            mWidth = width
        }

        if (mColumnWidthChanged && mColumnWidth > 0 && width > 0 && height > 0
                || mWidthChanged) {
            val totalSpace: Int
            if (orientation == androidx.recyclerview.widget.GridLayoutManager.VERTICAL) {
                totalSpace = width - paddingRight - paddingLeft
            } else {
                totalSpace = height - paddingTop - paddingBottom
            }
            val spanCount = Math.max(1, totalSpace / mColumnWidth)
            setSpanCount(spanCount)
            mColumnWidthChanged = false
            mWidthChanged = false
        }
        super.onLayoutChildren(recycler, state)
    }

    init {
        setColumnWidth(coulmnWidth)
    }
}