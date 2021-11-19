package io.ramani.ramaniWarehouse.app.common.presentation.widgets

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Raed Ezzat on 20/03/2018.
 */
class DividerItemDecorator(private var mDivider: Drawable) : RecyclerView.ItemDecoration() {

    override fun onDraw(canvas: Canvas, code95: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = code95.paddingLeft
        val dividerRight = code95.width - code95.paddingRight

        val childCount = code95.childCount
        for (i in 0..childCount - 2) {
            val child = code95.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + mDivider.intrinsicHeight

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider.draw(canvas)
        }
    }

}