package io.ramani.ramaniWarehouse.app.common.presentation.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniWarehouse.domainCore.log.logWarning
import kotlin.math.roundToInt


/**
 * Created by Raed Ezzat on 20/03/2018.
 */
class DefaultDividerItemDecoration(context: Context, private val orientation: Int) : DividerItemDecoration(context, orientation) {

    private val attrs = intArrayOf(android.R.attr.listDivider)

    private val mBounds = Rect()

    private var mDivider: Drawable? = null

    init {
        val a = context.obtainStyledAttributes(attrs)
        mDivider = a.getDrawable(0)
        if (mDivider == null) {
            logWarning("@android:attr/listDivider was not set in the theme used for this " + "DividerItemDecoration. Please set that attribute all call setDrawable()")
        }
        a.recycle()
        setOrientation(orientation)
    }

    override fun onDraw(canvas: Canvas, rv: RecyclerView, state: RecyclerView.State) {
        if (rv.layoutManager == null || mDivider == null) {
            return
        }

        mDivider?.let { divider ->
            if (orientation == VERTICAL) {
                drawVertical(canvas, rv, divider)
            } else {
                drawHorizontal(canvas, rv, divider)
            }
        }
    }

    private fun drawVertical(canvas: Canvas, rv: RecyclerView, divider: Drawable) {
        canvas.save()
        val left: Int
        val right: Int

        if (rv.clipToPadding) {
            left = rv.paddingLeft
            right = rv.width - rv.paddingRight
            canvas.clipRect(left, rv.paddingTop, right,
                    rv.height - rv.paddingBottom)
        } else {
            left = 0
            right = rv.width
        }

        val childCount = rv.childCount
        for (i in 0 until childCount) {
            if (shouldDraw(i, childCount)) {
                val child = rv.getChildAt(i)
                rv.getDecoratedBoundsWithMargins(child, mBounds)
                val bottom = mBounds.bottom + child.translationY.roundToInt()
                val top = bottom - divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, rv: RecyclerView, divider: Drawable) {
        canvas.save()
        val top: Int
        val bottom: Int

        if (rv.clipToPadding) {
            top = rv.paddingTop
            bottom = rv.height - rv.paddingBottom
            canvas.clipRect(rv.paddingLeft, top,
                    rv.width - rv.paddingRight, bottom)
        } else {
            top = 0
            bottom = rv.height
        }

        val childCount = rv.childCount
        for (i in 0 until childCount) {
            if (shouldDraw(i, childCount)) {
                val child = rv.getChildAt(i)
                rv.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
                val right = mBounds.right + child.translationX.roundToInt()
                val left = right - divider.intrinsicWidth
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    private fun shouldDraw(index: Int, childCount: Int) =
            (0 until childCount - 1).contains(index)

}